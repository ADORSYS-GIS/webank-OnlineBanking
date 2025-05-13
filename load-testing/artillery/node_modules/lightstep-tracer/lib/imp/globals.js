'use strict';

var _createClass = function () { function defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } } return function (Constructor, protoProps, staticProps) { if (protoProps) defineProperties(Constructor.prototype, protoProps); if (staticProps) defineProperties(Constructor, staticProps); return Constructor; }; }();

var _each2 = require('../_each');

var _each3 = _interopRequireDefault(_each2);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

var PackageGlobals = function () {
    function PackageGlobals() {
        _classCallCheck(this, PackageGlobals);

        this.options = {};
    }

    _createClass(PackageGlobals, [{
        key: 'setOptions',
        value: function setOptions(opts) {
            var _this = this;

            (0, _each3.default)(opts, function (val, key) {
                _this.options[key] = val;
            });
        }
    }]);

    return PackageGlobals;
}();

module.exports = new PackageGlobals();

//# sourceMappingURL=globals.js.map