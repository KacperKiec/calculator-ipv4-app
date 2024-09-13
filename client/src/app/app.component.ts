import {Component, EventEmitter, Input, Output} from '@angular/core';
import { RouterOutlet } from '@angular/router';
import {ToolListComponent} from "./tool-list/tool-list.component";
import {ResultListComponent} from "./result-list/result-list.component";
import {Subnet} from "./types/subnet";
import {FormsModule} from "@angular/forms";
import {ErrorViewComponent} from "./error-view/error-view.component";

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, ToolListComponent, ResultListComponent, FormsModule, ErrorViewComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {

  public address: string;
  public mask: string;

  @Input() result: Subnet[];
  @Output() emitResult = new EventEmitter<Subnet[]>();

  title = 'calculatorTS';

  constructor() {
    this.address = '';
    this.mask = '';
    this.result = [];
  }

  getResult(value: Subnet[]) {
    this.result = value;
    this.emitResult.emit(value);
  }
}
