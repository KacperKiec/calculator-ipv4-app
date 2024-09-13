import {Component, EventEmitter, Output} from '@angular/core';
import {ActionType} from "../types/action";

@Component({
  selector: 'app-param-controller',
  standalone: true,
  imports: [],
  templateUrl: './param-controller.component.html',
  styleUrl: './param-controller.component.css'
})
export class ParamControllerComponent {

  @Output() confirmAction = new EventEmitter<[ActionType, number[]]>();

  private isParam: boolean;
  private isOptimalParam: boolean;
  private subnetSizes: number[];

  constructor() {
    this.isParam = false;
    this.isOptimalParam = false;
    this.subnetSizes = [];
  }

  public addParam(msg: string){
    if(!this.isParam) {
      const paramElement = document.createElement('input');
      const confirmButton = document.createElement('button');

      paramElement.type = 'number';
      paramElement.style.width = '130px';
      paramElement.style.textAlign = 'center';
      paramElement.classList.add('param');
      paramElement.placeholder = msg;
      paramElement.max = '65534';
      paramElement.min = '1';

      confirmButton.style.textAlign = 'center';
      confirmButton.style.border = '1px solid #ccc';
      confirmButton.style.boxSizing = 'border-box';
      confirmButton.style.marginTop = '10px';
      confirmButton.classList.add('confirm-button');
      confirmButton.textContent = 'Oblicz';

      confirmButton.addEventListener('click', () => {
        if (paramElement.value !== '') {
          paramElement.style.border = '';
          paramElement.style.backgroundColor = '';

          const paramElementValueTab: number[] = [Number(paramElement.value)];
          if (msg === 'Liczba hostów') this.confirmAction.emit(['hosts', paramElementValueTab]);
          if (msg === 'Liczba podsieci') this.confirmAction.emit(['nets', paramElementValueTab]);
        } else {
          paramElement.style.border = '2px solid red';
          paramElement.style.backgroundColor = '#ffe6e6';
        }
      });

      const cont = document.querySelector('.param-container');
      if (cont){
        cont.appendChild(paramElement);
        cont.appendChild(confirmButton);
      }
      this.isParam = true;

    } else {
      const paramElement: HTMLInputElement = document.querySelector('.param') as HTMLInputElement;
      if (paramElement){
        paramElement.placeholder = msg;
      } paramElement.value = '';
    }
  }

  public removeParam() {
    const paramInput = document.querySelector('.param');
    const confirmButton = document.querySelector('.confirm-button');

    const cont = document.querySelector('.param-container');
    if (cont) {
      if (paramInput) cont.removeChild(paramInput);
      if (confirmButton) cont.removeChild(confirmButton)
      this.isParam = false;
    }
  }

  public createOptimalParam() {
    const paramInputElement = document.createElement('input');
    const paramButtonElement = document.createElement('button');
    const confirmButton = document.createElement('button');

    paramInputElement.type = 'number';
    paramInputElement.max = '65534';
    paramInputElement.min = '1';
    paramInputElement.placeholder = 'Liczba hostów';
    paramInputElement.style.width = '130px';
    paramInputElement.style.textAlign = 'center';
    paramInputElement.classList.add('param');

    confirmButton.style.textAlign = 'center';
    confirmButton.style.border = '1px solid #ccc';
    confirmButton.style.boxSizing = 'border-box';
    confirmButton.style.marginTop = '10px';
    confirmButton.classList.add('confirm-button');
    confirmButton.textContent = 'Oblicz';

    confirmButton.addEventListener('click', () => {
      if (this.subnetSizes.length !== 0) {
        this.confirmAction.emit(['optimal', this.subnetSizes]);
        confirmButton.style.border = '';
        confirmButton.style.backgroundColor = '';
      } else {
        confirmButton.style.border = '2px solid red';
        confirmButton.style.backgroundColor = '#ffe6e6';
      }
    });

    paramButtonElement.textContent = 'Dodaj';
    paramButtonElement.style.textAlign = 'center';
    paramButtonElement.style.border = '1px solid #ccc';
    paramButtonElement.style.boxSizing = 'border-box';
    paramButtonElement.style.marginLeft = '15px';
    paramButtonElement.classList.add('param-button');
    paramButtonElement.addEventListener('click', () => {
      if (paramInputElement.value !== '') {
        paramInputElement.style.border = '';
        paramInputElement.style.backgroundColor = '';

        this.subnetSizes.push(Number(paramInputElement.value));

        const hostListElement = document.createElement('li');
        hostListElement.textContent = `${paramInputElement.value} hosty / ów`;
        hostListElement.classList.add('host-list-element');

        let hostList = document.querySelector('.host-list');

        if (!hostList) {
          hostList = document.createElement('ul');
          hostList.classList.add('host-list');

          const cont = document.querySelector('.param-container');
          if (cont) cont.appendChild(hostList);

        }

        hostList.appendChild(hostListElement)
      } else {
        paramInputElement.style.border = '2px solid red';
        paramInputElement.style.backgroundColor = '#ffe6e6';

      }
    });

    const inputBar = document.createElement('div');
    inputBar.classList.add('input-bar');
    inputBar.style.display = 'flex';
    inputBar.style.flexDirection = 'row';
    inputBar.style.textAlign = 'center';
    inputBar.style.justifyContent = 'center';

    inputBar.appendChild(paramInputElement);
    inputBar.appendChild(paramButtonElement);

    const cont = document.querySelector('.param-container');
    if (cont) {
      cont.appendChild(inputBar);
      cont.appendChild(confirmButton);
    }
    this.isOptimalParam = true;
  }

  public removeOptimalParam() {
    const paramBar = document.querySelector('.input-bar');
    const paramList = document.querySelector('.host-list');
    const hostListItems = document.querySelectorAll('.host-list-element');
    const confirmButton = document.querySelector('.confirm-button');

    const cont = document.querySelector('.param-container');
    if (cont){
      if (paramBar) cont.removeChild(paramBar);
      hostListItems.forEach(item => {
        if (item.parentNode) {
          item.parentNode.removeChild(item);
        }
      });
      if (paramList) cont.removeChild(paramList);
      if (confirmButton) cont.removeChild(confirmButton);

      this.subnetSizes = [];
      this.isOptimalParam = false;

    }
  }
}
