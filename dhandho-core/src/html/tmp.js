var jsdom = require('jsdom');

const { window } = new jsdom.JSDOM('<html><body><div id="mainDiv">MainDiv</div></body></html>');
const { document } = window;
var $ = require('jquery')(window);
