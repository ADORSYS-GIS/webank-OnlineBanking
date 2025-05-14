const crypto = require('crypto');
const CryptoJS = require('crypto-js');

// Preload jose dynamically to avoid async in processor functions
let jose;
(async () => {
  jose = await import('jose');
})();

function hashPayload(payload) {
  return CryptoJS.SHA256(payload).toString(CryptoJS.enc.Hex);
}

function generateTestKeyPair(context, events, done) {
  console.log('Generating test key pair...');
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
}

function generateTestData(context, events, done) {
  console.log('Generating test data...');
  if (!jose) {
    return done(new Error('jose module not loaded yet'));
  }
  try {
    const { privateKeyJWK, publicKeyJWK } = context.vars.keyPair;
    const accountId = crypto.randomBytes(8).toString('hex');
    const recipientAccountId = crypto.randomBytes(8).toString('hex');
    const amount = `${Math.floor(Math.random() * (1000 - 100 + 1) + 100)}.00`;

    const accountCertPayload = { hash: hashPayload(accountId) };
    const accountCertHeader = { typ: 'JWT', alg: 'ES256', jwk: publicKeyJWK };

    // Wrap async jose calls in a Promise and resolve synchronously
    Promise.resolve()
      .then(async () => {
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
      })
      .catch(error => {
        console.error('Error generating test data:', error);
        done(error);
      });
  } catch (error) {
    console.error('Error generating test data:', error);
    done(error);
  }
}

function generateJWT(context, events, done) {
  console.log('Generating JWT for scenario:', context.vars.scenarioName || 'unknown');
  if (!jose) {
    return done(new Error('jose module not loaded yet'));
  }
  try {
    const { privateKeyJWK, publicKeyJWK } = context.vars.keyPair;
    const { testData } = context.vars;
    const scenarioName = context.vars.scenarioName || '';

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
      dataToHash = testData.accountId;
      requestBody = { accountID: testData.accountId };
      headerFields = { accountJwt: testData.accountCert };
    } else if (scenarioName.includes('Money Transfer Flow')) {
      dataToHash = [testData.accountId, testData.amount, testData.recipientAccountId].join('');
      requestBody = {
        recipientAccountId: testData.accountId,
        senderAccountId: testData.recipientAccountId,
        amount: testData.amount,
      };
      headerFields = { accountJwt: testData.accountCert, kycJwt: testData.accountCert };
    } else if (scenarioName.includes('Account Recovery Flow')) {
      dataToHash = testData.accountId;
      requestBody = { accountId: testData.accountId };
      headerFields = { accountJwt: testData.accountCert };
    } else {
      console.warn('Unknown scenario, using default empty request');
      dataToHash = '';
      requestBody = {};
      headerFields = { devJwt: testData.accountCert };
    }

    context.vars.requestBody = requestBody;
    const hash = hashPayload(dataToHash);

    const jwtPayload = { hash };
    const header = { typ: 'JWT', alg: 'ES256', jwk: publicKeyJWK, ...headerFields };
    console.log('JWT header before signing:', JSON.stringify(header, null, 2));

    if (!header.accountJwt && !header.devJwt && !header.phoneNumberJwt) {
      throw new Error('Missing required JWT header. Need one of: accountJwt, devJwt, phoneNumberJwt.');
    }

    // Wrap async jose calls in a Promise
    Promise.resolve()
      .then(async () => {
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
      })
      .catch(error => {
        console.error('Error generating JWT:', error);
        done(error);
      });
  } catch (error) {
    console.error('Error generating JWT:', error);
    done(error);
  }
}

module.exports = {
  generateTestKeyPair,
  generateTestData,
  generateJWT,
};
