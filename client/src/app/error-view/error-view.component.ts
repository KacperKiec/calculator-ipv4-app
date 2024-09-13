import {Component, Input, OnInit} from '@angular/core';
import {NgClass} from "@angular/common";
import {ErrorHandlerService} from "../error-handler-service/error-handler.service";

@Component({
  selector: 'app-error-view',
  standalone: true,
  imports: [
    NgClass
  ],
  templateUrl: './error-view.component.html',
  styleUrl: './error-view.component.css'
})
export class ErrorViewComponent implements OnInit {
  @Input() errorMessage: string = '';
  public isVisible;

  constructor(private errorHandlerService: ErrorHandlerService) {
    this.isVisible = false;
  }

  ngOnInit() {
    this.errorHandlerService.observableError.subscribe((errorMessage) => {
      if (errorMessage) {
        this.text = errorMessage;
        this.visible = true;
      } else {
        this.clearError();
      }
    });
  }

  private set visible(visible: boolean) {
    this.isVisible = visible;
  }

  private set text(text: string) {
    const message = document.querySelector('.message-container');
    if (message) {
      message.textContent = text;
    }
  }

  private clearError() {
    this.visible = false;
    this.text = ''
  }
}
