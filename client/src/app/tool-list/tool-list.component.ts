import {
  Component,
  ElementRef,
  EventEmitter,
  Input,
  OnDestroy,
  OnInit,
  Output,
  ViewChild
} from '@angular/core';
import {NgFor} from "@angular/common";
import {Subnet} from "../types/subnet";
import {ApiService} from "../api-service/api.service";
import {ParamControllerComponent} from "../param-controller/param-controller.component";
import {ActionType} from "../types/action";


@Component({
  selector: 'app-tool-list',
  standalone: true,
  imports: [NgFor, ParamControllerComponent],
  templateUrl: './tool-list.component.html',
  styleUrl: './tool-list.component.css'
})
export class ToolListComponent implements OnInit, OnDestroy {

  @Input() address: string;
  @Input() mask: string;

  @Output() emitResult = new EventEmitter<Subnet[]>();
  @ViewChild(ParamControllerComponent) paramController: ParamControllerComponent;

  public dropdownStatus: boolean;
  public modes: string[] = [
    'Adres sieci',
    'Adres rozgłoszeniowy',
    'Podsieci ze względu na liczbę podsieci',
    'Podsieci ze względu na liczbę hostów',
    'Podsieci ze zmienną maską'
  ];

  constructor(private elementRef: ElementRef, private apiService: ApiService) {
    this.address = '';
    this.mask = '';
    this.dropdownStatus = false;
    this.paramController = new ParamControllerComponent();
  }

  ngOnInit() {
    document.addEventListener('click', this.onClickOutside.bind(this));
  }

  ngOnDestroy() {
    document.removeEventListener('click', this.onClickOutside.bind(this));
  }

  onClickOutside(event: Event) {
    if (!this.elementRef.nativeElement.contains(event.target)) {
      this.dropdownStatus = false;
    }
  }

  toggleDropdown(){
    this.dropdownStatus = !this.dropdownStatus;
  }

  selectFunction(mode: string) {
    this.dropdownStatus = false;

    if (!this.verifyAddress()) {
      const inputFrame= document.querySelector('#address input') as HTMLElement;
      if (inputFrame) {
        inputFrame.style.border = '2px solid red';
        inputFrame.style.backgroundColor = '#ffe6e6';

        alert('Podany adres ma nieprawidłowy format!');
        return;
      }
    } else {
      const inputFrame= document.querySelector('#address input') as HTMLElement;
      inputFrame.style.border = '1px solid #ccc';
      inputFrame.style.backgroundColor = '';
    }

    if (!this.mask) {
      const maskFrame = document.querySelector('#mask select') as HTMLElement;
      if (maskFrame) {
        maskFrame.style.border = '2px solid red';
        maskFrame.style.backgroundColor = '#ffe6e6';

        alert('Podaj maskę adresu!');
        return;
      }
    } else {
      const maskFrame= document.querySelector('#mask select') as HTMLElement;
      maskFrame.style.border = '1px solid #ccc';
      maskFrame.style.backgroundColor = '';
    }


    if(mode === 'Adres sieci') {
      this.paramController.removeOptimalParam();
      this.paramController.removeParam();

      this.cleanResult();

      this.apiService.getNetAddress(this.address, this.mask)
        .subscribe(subnet => {
          this.emitResult.emit([subnet]);
        });

    } else if (mode === 'Adres rozgłoszeniowy') {
      this.paramController.removeOptimalParam();
      this.paramController.removeParam();

      this.cleanResult();

      this.apiService.getBroadcastAddress(this.address, this.mask)
        .subscribe(subnet => {
          this.emitResult.emit([subnet]);
        });

    } else if(mode === 'Podsieci ze względu na liczbę podsieci'){
      this.paramController.removeOptimalParam();
      this.paramController.removeParam();
      this.paramController.addParam('Liczba podsieci');

    } else if(mode === 'Podsieci ze względu na liczbę hostów') {
      this.paramController.removeOptimalParam();
      this.paramController.removeParam();
      this.paramController.addParam('Liczba hostów');

    } else if (mode === 'Podsieci ze zmienną maską') {
      this.paramController.removeOptimalParam();
      this.paramController.removeParam();
      this.paramController.createOptimalParam();

    }
  }

  executeAction(params: [ActionType, number[]]) {
    this.cleanResult();

    if (params[0] === 'hosts') {

      this.apiService.getSubnetsHosts(this.address, this.mask, <number>params[1]?.[0])
        .subscribe(subnet => {
          this.emitResult.emit(subnet);
        });
    } else if (params[0] === 'nets') {

      this.apiService.getSubnetsNets(this.address, this.mask, <number>params[1]?.[0])
        .subscribe(subnet => {
          this.emitResult.emit(subnet);
        });
    } else if (params[0] === 'optimal') {

      this.apiService.getSubnetsOptimal(this.address, this.mask, <number[]>params[1])
        .subscribe(subnet => {
          this.emitResult.emit(subnet);
        });
    }

    this.paramController.removeOptimalParam();
    this.paramController.removeParam();
  }

  verifyAddress(): boolean {
    const addressTab: string[] = this.address.split('.');

    if (addressTab.length !== 4) return false;

    for (let part of addressTab) {
      const temp = Number(part);
      if (isNaN(temp) || temp < 0 || temp > 255) return false;
    }

    return true;
  }

  cleanResult() {
    this.emitResult.emit([]);
  }

}

