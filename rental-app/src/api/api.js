import axios from "axios";
import cookie from "react-cookies";

const BASE_URL = 'http://localhost:8080/api';

export const endpoints = {
    'register': '/user',
    'login': '/token',
    'current-user': '/current-user',

    'createMotel': '/createMotel',
    'rentedMotel':"/rentedMotel",
    
    'follow': '/follow/follow',
    'unfollow': '/follow/unfollow',

    'review': '/review',

    'comment': '/comment',

    'package':'/package',
    'bill':'/bill/create',
    
    'suggest_keyword':"/suggest_keyword",
}


export const authApi = () => {
    const token = cookie.load('token')
    console.log(token)
    return axios.create({
        baseURL: BASE_URL,
        headers: {
            Authorization: `Bearer ${token}`
        }
    })
}

export default axios.create({
    baseURL: BASE_URL
});