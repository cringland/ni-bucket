const fs = require('fs-extra');

fs.copy('./dist/', '../target/classes/public/');
