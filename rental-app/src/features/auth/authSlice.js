import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';
import APIs, { authApi, endpoints } from "../../api/api";
import axios from 'axios';
import { AuthCredential } from 'firebase/auth';
import { Form } from 'react-router-dom';
import cookie from "react-cookies";


// Async thunk để xử lý đăng nhập
export const loginUser = createAsyncThunk('auth/loginUser', async (credentials, { rejectWithValue }) => {
  try {
    const response = await APIs.post(endpoints['login'], credentials);
    const token = response.data;
    cookie.save("token", token) 
    return response.data;
  } catch (error) {
    return rejectWithValue(error.response.data);
  }
});

// Async thunk để lấy thông tin người dùng
export const fetchUserDetails = createAsyncThunk('auth/fetchUserDetails', async (_, { rejectWithValue }) => {
  try {
    const response = await authApi().get(endpoints['current-user'])
    localStorage.setItem('user', response.data);  // Save token in localStorage for persistence
    return response.data;
  } catch (error) {
    return rejectWithValue(error.response.data);
  }
});

const authSlice = createSlice({
  name: 'auth',
  initialState: {
    isAuthenticated: !!localStorage.getItem('user'),  // Check localStorage initially
    token: null,
    user: null,
    role: null,
    status: 'idle',
    error: null,
    isLocked: false,
  },
  reducers: {
    logout: (state) => {
      state.isAuthenticated = false;
      state.token = null;
      state.user = null;
      state.isLocked = false;
      localStorage.removeItem('token');
      cookie.remove("token")
      cookie.remove("userFirebase")
    },
  },
  extraReducers: (builder) => {
    builder
      .addCase(loginUser.pending, (state) => {
        state.status = 'loading';
        state.error = null
      })
      .addCase(loginUser.fulfilled, (state, action) => {
        state.status = 'succeeded';
        state.token = action.payload;
      })
      .addCase(loginUser.rejected, (state, action) => {
        state.status = 'failed';
        state.error = action.payload;
      })
      .addCase(fetchUserDetails.pending, (state) => {
        state.status = 'loading';
        state.error = null
      })
      .addCase(fetchUserDetails.fulfilled, (state, action) => {
        state.status = 'succeeded';
        state.user = action.payload;
        state.role = action.payload.userole;
        if (!action.payload.active) {
          state.isLocked = true; // Set the lock state
        }
        console.log(state.user)
      })
      .addCase(fetchUserDetails.rejected, (state, action) => {
        state.status = 'failed';
        state.error = action.payload;
      });
  },
});

export const { logout } = authSlice.actions;
export default authSlice.reducer;
