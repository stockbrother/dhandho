require('require-global')
var jsdom = require('jsdom');
const { JSDOM } = jsdom;
const { window } = new JSDOM('<html></html>');
const { document } = window.document;
var $ = require('jquery')(window);
