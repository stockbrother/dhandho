import { FormsModule } from '@angular/forms';
import { HttpModule, BaseRequestOptions, Http, XHRBackend, Response, ResponseOptions } from '@angular/http';
import { MockBackend } from '@angular/http/testing';
import { LoggerService } from '../service/logger.service';
import { NgModule, DebugElement } from '@angular/core';
import { TestBed, ComponentFixture, async, fakeAsync, tick } from '@angular/core/testing';
import { CommandComponent } from './command.component';
import { By } from '@angular/platform-browser';

describe( 'CommandComponent', () => {
    let mockBackend: MockBackend;

    let comp: CommandComponent;

    let fixture: ComponentFixture<CommandComponent>;

    let de: DebugElement;

    let ne: any;

    beforeEach(() => {
        TestBed.configureTestingModule( {
            imports: [FormsModule, HttpModule],
            declarations: [
                CommandComponent
            ],
            providers: [
                LoggerService,
                MockBackend,
                BaseRequestOptions,
                {
                    provide: XHRBackend,
                    useClass: MockBackend
                },
                {
                    provide: Http,
                    useFactory: ( backend, options ) => new Http( backend, options ),
                    deps: [MockBackend, BaseRequestOptions, XHRBackend]
                }
            ]
        } ).compileComponents();
        mockBackend = TestBed.get( MockBackend );
        fixture = TestBed.createComponent( CommandComponent );
        de = fixture.debugElement;
        comp = de.componentInstance;
        ne = de.nativeElement;
    } );

    it( 'Should create the command', async(() => {
        expect( comp ).toBeTruthy();
    } ) );

    it( 'Title check', async(() => {
        const compiled = fixture.debugElement.nativeElement;
        it( 'Title should not be binded until fixture.detectChanges()', () => {
            expect( compiled.querySelector( 'h1' ).textContent ).toEqual( '' );
        } );

        fixture.detectChanges();
        expect( compiled.querySelector( 'h1' ).textContent ).toContain( comp.title );
    } ) );

    it( 'Response Processing', fakeAsync(() => {
        let response = {
            'type': 'table',
            'columnNames': ['corpId', 'corpName', 'highLight', 'ProfitMargin', 'AssetTurnover', 'EquityMultiplier'],
            'rowArray': [
                ['000001', 'Name1', false, 0.01, 0.11, 1.1],
                ['000002', 'Name2', true, 0.02, 0.12, 1.2],
                ['000003', 'Name3', false, 0.03, 0.13, 1.3],
                ['000004', 'Name4', false, 0.04, 0.14, 1.4],
            ]
        };
        let jsonS: string = JSON.stringify( response );
        console.log( jsonS );
        mockBackend.connections.subscribe( connection => {

            let res: Response = new Response( <ResponseOptions>{
                body: jsonS,
                status: 200
            } );
            connection.mockRespond( res );
        } );
        //
        expect( comp.responseArray.length ).toEqual( 0 );

        // Simulate execute command button click.
        {
            comp.command = 'dupont -r -c 000001 -y 2016 -f 0.01';
            let button: DebugElement = fixture.debugElement.query( By.css( 'button' ) );
            button.triggerEventHandler( 'click', null );
            fixture.detectChanges();
            tick();
            expect( comp.responseArray.length ).toEqual( 1 );
            fixture.detectChanges();
        }
        {
            comp.command = 'dupont -r -c 000002 -y 2016 -f 0.01';
            let button: DebugElement = fixture.debugElement.query( By.css( 'button' ) );
            button.triggerEventHandler( 'click', null );
            fixture.detectChanges();
            tick();
            expect( comp.responseArray.length ).toEqual( 2 );
            fixture.detectChanges();
        }

    } ) );




} );

