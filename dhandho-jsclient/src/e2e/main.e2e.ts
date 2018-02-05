import http = require('http');
import { JasmineDescriber } from '../app/dhojsw/util/jasmine.describer';
import { HttpServerE2e } from './http.server.e2e';
import { HttpBackendE2e } from './httpbackend.e2e';

// new HttpServerE2e().describe();

new HttpBackendE2e().describe();
