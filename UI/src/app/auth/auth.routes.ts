import { SigninComponent } from './signin/signin.component';
import { SignupComponent } from './signup/signup.component';
import { NonAuthGuardService } from '../services/non-auth-guard.service';

export const AuthRoutes = [
  { path: 'login', component: SigninComponent, canActivate: [NonAuthGuardService] },
  { path: 'signup', component: SignupComponent, canActivate: [NonAuthGuardService] }
 
];
