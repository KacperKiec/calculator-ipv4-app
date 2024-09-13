import {Component, Input} from '@angular/core';
import {Subnet} from "../types/subnet";
import {NgForOf} from "@angular/common";

@Component({
  selector: 'app-result-list',
  standalone: true,
  imports: [
    NgForOf
  ],
  templateUrl: './result-list.component.html',
  styleUrl: './result-list.component.css'
})
export class ResultListComponent {
  @Input() subnets: Subnet[];

  constructor() {
    this.subnets = [];
  }
}
