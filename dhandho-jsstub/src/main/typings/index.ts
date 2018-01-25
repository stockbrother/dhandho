


import { async } from '@angular/core/testing';


export function jasmine_describe( desc: string, fn: Function ): void {
    describe( desc, () => { fn.call( null ); } );
}
export function jasmine_it( desc: string, fn: Function ): void {
    it( desc, () => { fn.call( null ); } );
}

export function angular_async( fn: Function ): Function {
    return async( fn );
}
