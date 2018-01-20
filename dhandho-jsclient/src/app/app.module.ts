import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';


import { AppComponent } from './app.component';
import { CommandComponent } from './command/command.component';


@NgModule( {
    declarations: [
        AppComponent,
        CommandComponent
    ],
    imports: [
        BrowserModule, FormsModule, HttpModule
    ],
    providers: [],
    bootstrap: [AppComponent]
} )
export class AppModule { }
