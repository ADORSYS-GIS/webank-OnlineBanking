const crypto = require('crypto');
const jose = require('jose');
const CryptoJS = require('crypto-js');
const axios = require('axios');

function hashPayload(payload) {
    return CryptoJS.SHA256(payload).toString(CryptoJS.enc.Hex);
}

function generateTestKeyPair(context, events, done) {
    console.log('Generating test key pair...');
    (async () => {
        try {
            if (!process.env.SERVER_PRIVATE_KEY_JSON || !process.env.SERVER_PUBLIC_KEY_JSON) {
                throw new Error('Environment variables SERVER_PRIVATE_KEY_JSON and SERVER_PUBLIC_KEY_JSON must be set.');
            }

            const serverPrivateKeyJson = JSON.parse(process.env.SERVER_PRIVATE_KEY_JSON);
            const serverPublicKeyJson = JSON.parse(process.env.SERVER_PUBLIC_KEY_JSON);

            context.vars.keyPair = {
                privateKeyJWK: serverPrivateKeyJson,
                publicKeyJWK: serverPublicKeyJson,
            };
            console.log('Key pair set from environment variables:', context.vars.keyPair);
            done();
        } catch (error) {
            console.error('Error setting key pair:', error);
            done(error);
        }
    })();
}

function generateTestData(context, events, done) {
    console.log('Generating test data...');
    (async () => {
        try {
            const { privateKeyJWK, publicKeyJWK } = context.vars.keyPair;

            const accountId = context.vars.senderAccountId || crypto.randomBytes(8).toString('hex');
            const recipientAccountId = context.vars.recipientAccountId || crypto.randomBytes(8).toString('hex');
            const amount = `${Math.floor(Math.random() * (1000 - 100 + 1) + 100)}.00`;

            console.log('Using accountId:', accountId);
            console.log('Using recipientAccountId:', recipientAccountId);
            console.log('Generated amount:', amount);

            const accountCertPayload = {
                hash: hashPayload(accountId),
            };
            const accountCertHeader = { typ: 'JWT', alg: 'ES256', jwk: publicKeyJWK };
            const privateKey = await jose.importJWK(privateKeyJWK, 'ES256');
            const accountCert = await new jose.SignJWT(accountCertPayload)
                .setProtectedHeader(accountCertHeader)
                .sign(privateKey);

            const transactionJwtPayload = {
                hash: hashPayload([accountId, amount, recipientAccountId].join('')),
            };
            const transactionJwt = await new jose.SignJWT(transactionJwtPayload)
                .setProtectedHeader(accountCertHeader)
                .sign(privateKey);

            context.vars.testData = {
                accountId,
                recipientAccountId,
                amount,
                accountCert,
                transactionJwt,
            };
            console.log('Test data generated:', context.vars.testData);
            done();

        } catch (error) {
            console.error('Error generating test data:', error);
            done(error);
        }
    })();
}

function generateJWT(context, events, done) {
    console.log('Generating JWT for scenario:', context.vars.scenarioName || 'unknown');
    (async () => {
        try {
            const { privateKeyJWK, publicKeyJWK } = context.vars.keyPair;
            const { testData } = context.vars;
            const scenarioName = context.vars.scenarioName || '';

            const accountId = context.vars.senderAccountId || testData.accountId;
            const recipientAccountId = context.vars.recipientAccountId || testData.recipientAccountId;

            console.log('JWT generation - accountId:', accountId);
            console.log('JWT generation - recipientAccountId:', recipientAccountId);

            let dataToHash = '';
            let requestBody = {};
            let headerFields = {};

            if (scenarioName.includes('Account Registration Flow')) {
                dataToHash = '';
                requestBody = {
                    publicKey: JSON.stringify(publicKeyJWK),
                    registrationJwt: testData.accountCert,
                };
                headerFields = { devJwt: testData.accountCert };
            } else if (scenarioName.includes('Balance and Transaction Flow')) {
                dataToHash = accountId;
                requestBody = { accountID: accountId };
                headerFields = { accountJwt: testData.accountCert };
            } else if (scenarioName.includes('Money Transfer Flow')) {
                dataToHash = [accountId, testData.amount, recipientAccountId].join('');
                requestBody = {
                    recipientAccountId: recipientAccountId,
                    senderAccountId: accountId,
                    amount: testData.amount,
                };
                headerFields = { accountJwt: testData.accountCert, kycJwt: testData.accountCert };
            } else if (scenarioName.includes('Account Recovery Flow')) {
                dataToHash = accountId;
                requestBody = { accountId: accountId };
                headerFields = { accountJwt: testData.accountCert };
            } else {
                console.warn('Unknown scenario, using default empty request');
                dataToHash = '';
                requestBody = {};
                headerFields = { devJwt: testData.accountCert };
            }

            console.log('Data to hash:', dataToHash);
            console.log('Request body:', requestBody);

            context.vars.requestBody = requestBody;
            const hash = hashPayload(dataToHash);

            const jwtPayload = { hash };
            const header = { typ: 'JWT', alg: 'ES256', jwk: publicKeyJWK, ...headerFields };
            console.log('JWT header before signing:', JSON.stringify(header, null, 2));

            if (!header.accountJwt && !header.devJwt && !header.phoneNumberJwt) {
                throw new Error("Missing required JWT header. Need one of: accountJwt, devJwt, phoneNumberJwt.");
            }

            const privateKey = await jose.importJWK(privateKeyJWK, 'ES256');
            const jwt = await new jose.SignJWT(jwtPayload)
                .setProtectedHeader(header)
                .sign(privateKey);

            const [encodedHeader] = jwt.split('.');
            const decodedHeader = Buffer.from(encodedHeader, 'base64').toString('utf8');
            console.log('Encoded JWT header:', decodedHeader);
            context.vars.jwtToken = jwt;
            console.log('JWT generated:', jwt);
            done();
        } catch (error) {
            console.error('Error generating JWT:', error);
            done(error);
        }
    })();
}

async function registerAccount(context, events, done) {
    console.log('Registering account...');
    try {
        const { jwtToken, testData } = context.vars;

        const response = await axios.post('http://localhost:8081/api/registration', {
            publicKey: JSON.stringify(context.vars.keyPair.publicKeyJWK),
            registrationJwt: testData.accountCert,
        }, {
            headers: {
                Authorization: `Bearer ${jwtToken}`,
                'Content-Type': 'application/json',
            },
        });

        console.log('Registration response status:', response.status);
        console.log('Registration response body:', response.data);

        if (response.status === 201) {
            // Extract accountId from the third line of the response
            const lines = response.data.split('\n');
            if (lines.length >= 3) {
                context.vars.accountId = lines[2].trim();
                console.log('Captured accountId:', context.vars.accountId);
            } else {
                throw new Error('accountId not found in response (expected in third line)');
            }
        } else {
            throw new Error(`Registration failed with status ${response.status}`);
        }

        done();
    } catch (error) {
        console.error('Error registering account:', error.message);
        if (error.response) {
            console.error('Response status:', error.response.status);
            console.error('Response body:', error.response.data);
        }
        done(error);
    }
}

module.exports = {
    generateTestKeyPair,
    generateTestData,
    generateJWT,
    registerAccount,
};