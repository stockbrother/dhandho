import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';

import { AppComponent } from './app.component';
import { CommandComponent } from './command/command.component';
import { LoggerService } from './service/logger.service';

@NgModule( {
    declarations: [
        AppComponent,
        CommandComponent
    ],
    imports: [HttpModule,
        BrowserModule, FormsModule
    ],
    providers: [LoggerService],
    bootstrap: [AppComponent, CommandComponent]
} )
export class AppModule { }
