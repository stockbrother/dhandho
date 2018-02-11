import http = require('http');
import { JasmineDescriber } from '../app/util/jasmine.describer';
import { HttpServerE2e } from './http.server.e2e';
import { HttpBackendE2e } from './httpbackend.e2e';
import { NgApimockTest } from './ngapimock.e2e';

// new HttpServerE2e().describe();

// new HttpBackendE2e().describe();
new NgApimockTest().run();
