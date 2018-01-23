import { FormsModule } from '@angular/forms';
import { HttpClientModule, HttpClient, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { LoggerService } from '../service/logger.service';
import { NgModule, DebugElement } from '@angular/core';
import { TestBed, ComponentFixture, async, fakeAsync, tick } from '@angular/core/testing';
import { CommandComponent } from './command.component';
import { By } from '@angular/platform-browser';

describe( 'CommandComponent', () => {

    let httpMock: HttpTestingController;

    let comp: CommandComponent;

    let fixture: ComponentFixture<CommandComponent>;

    let de: DebugElement;

    let ne: any;

    beforeEach(() => {
        TestBed.configureTestingModule( {
            imports: [FormsModule, HttpClientTestingModule],
            declarations: [
                CommandComponent
            ],
            providers: [
                LoggerService
            ]
        } ).compileComponents();
        httpMock = TestBed.get( HttpTestingController );
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

        //
        expect( comp.responseArray.length ).toEqual( 0 );

        // Simulate execute command button click.
        {
            comp.command = 'dupont -r -c 000001 -y 2016 -f 0.01';
            let button: DebugElement = fixture.debugElement.query( By.css( 'button' ) );
            button.triggerEventHandler( 'click', null );

            const req = httpMock.expectOne( { method: 'POST' } );
            req.flush( response );
            fixture.detectChanges();
            tick();
            expect( comp.responseArray.length ).toEqual( 1 );
            fixture.detectChanges();
        }
    } ) );




} );

