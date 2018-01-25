


import { async } from '@angular/core/testing';


export function jasmine_describe( desc: string, fn: Function ): void {
    describe( desc, () => { fn.call( null ); } );
}

export function angular_async( fn: Function ): Function {
    return async( fn );
}

export class DhoTester {
    public angular_async( run: Function ): Function {
        return run;
    }

    public jasmine_describe( desc: string, run: () => void ) {

    }
}
