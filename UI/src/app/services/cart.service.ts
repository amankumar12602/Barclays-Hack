import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { config } from '../../config/local';
import { Cart } from '../store/model';


@Injectable()
export class CartService {

  url = `${config.apiUrl}/api/public/cart`;

  constructor(private httpClient: HttpClient) {
  }

  getCart() {
    return this.httpClient.get<Cart>(this.url);
  }

  postCart(productVariantId: number, amount: string) {
    return this.httpClient.post<Cart>(this.url, {
      productVariantId:1,
      amount:230
    });
  }

  incrementCartItem(productVariantId: number, amount: string) {
    return this.httpClient.post<Cart>(`${this.url}/public/increment`, {
      productVariantId,
      amount
    });
  }

  decrementCartItem(cartItemId: number, amount: string) {
    return this.httpClient.post<Cart>(`${this.url}/public/decrement`, {
      cartItemId,
      amount: Number(amount)
    });
  }

  removeFromCart(id: number) {
    return this.httpClient.post<Cart>(this.url + '/public/remove', {
      cartItemId: id
    });
  }

  confirmCart(cart: Cart) {
    return this.httpClient.post(this.url + '/public/confirm', cart);
  }

  applyDiscount(code: string) {
    return this.httpClient.post<Cart>(`${this.url}/public/discount`, {
      code
    });
  }

  emptyCart() {
    return this.httpClient.delete(this.url);
  }


}
