import React, { useEffect, useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { loginUser, fetchUserDetails } from '../features/auth/authSlice';
// import { loginUser as loginUserFirebase } from './auth/authFirebasSlice';
import { Container, TextField, Button, Typography, Box, Paper } from '@mui/material';
import Spiner from './Spiner'
import { Link, useNavigate } from 'react-router-dom';
import cookie from "react-cookies";
import { getAuth, signInWithEmailAndPassword, FacebookAuthProvider, signInWithPopup } from 'firebase/auth';

const Login = () => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const dispatch = useDispatch();
  const auth = useSelector((state) => state.auth.auth);

  const navigate = useNavigate();
  const authFB = getAuth();
  // const { user, loading, error } = useSelector((state) => state.authFirebase);

  const handleSubmit = async (e) => {
    try {
    e.preventDefault();
    const resultAction = await dispatch(loginUser({ username, password }));
    if (loginUser.fulfilled.match(resultAction)) {
      const r = await dispatch(fetchUserDetails());
      // Đăng nhập firebase
      const rs = await signInWithEmailAndPassword(authFB, r.payload.email, password);
      cookie.save("userFirebase", rs.user)
      if (!r.payload.active) {
        navigate('/account-locked');
      }
      else {
        navigate('/');
      }
    } else {
      // Đăng nhập thất bại
      console.log('Login failed:', resultAction.payload);
    }}catch (error) {
      console.error('Error during login:', error);
    }
  };

  const handleLoginFB = () => {
    const provider = new FacebookAuthProvider();
    signInWithPopup(authFB, provider)
      .then((result) => {
        cookie.save("userFirebase", result.user)
        console.log(result);

      })
      .catch((error) => {
        console.error(error);
      })
  }

  return (
    <Container maxWidth="sm">
      <Paper elevation={3} sx={{ padding: 3, marginTop: 3 }}>
        <Typography
          variant="h4"
          gutterBottom
          color="primary"
          align="center"
          style={{
            fontWeight: 'bold',
            textTransform: 'uppercase',
            marginTop: '20px'
          }}
        >
          Đăng Nhập
        </Typography>
        <Box component="form" onSubmit={handleSubmit} noValidate sx={{ mt: 1 }}>
          <TextField
            required
            fullWidth
            id="username"
            label="Tài khoản"
            name="username"
            autoComplete="username"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
            sx={{ mb: 2 }}
          />
          <TextField
            required
            fullWidth
            id="password"
            label="Mật khẩu"
            name="password"
            type="password"
            autoComplete="current-password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            sx={{ mb: 2 }}
          />
          {auth.status === 'loading' && <Spiner />}
          {auth.error && <Typography color="error">Tài khoản hoặc mật khẩu không chính xác !!</Typography>}
          <Button
            type="submit"
            fullWidth
            variant="contained"
            sx={{ mt: 3, mb: 2 }}
          >
            Đăng Nhập
          </Button>
          {/* <Button
            onClick={handleLoginFB}
            fullWidth
            variant="contained"
            sx={{ mt: 3, mb: 2 }}
          >
            Login With Facebook
          </Button> */}
 <Typography align="center" sx={{ mt: 2 }}>
        <Link to="/forgot-password" style={{ textDecoration: 'none', color: 'blue' }}>
          Quên mật khẩu?
        </Link>
      </Typography>
        </Box>
      </Paper>
    </Container>
  );
};

export default Login;
