// rootReducer.js
import { combineReducers } from 'redux';
import authReducer from './auth/authSlice';
// import authFirebaseSlice from './auth/authFirebaseSlice';

const rootReducer = combineReducers({
    auth: authReducer,
    // authFirebase: authFirebaseSlice,
});

export default rootReducer;
