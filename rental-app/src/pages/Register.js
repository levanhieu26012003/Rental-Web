import React, { useState } from 'react';
import { Container, TextField, Button, Typography, Box, Paper, Avatar } from '@mui/material';
import APIs, { endpoints } from "../api/api";
import { useNavigate } from 'react-router-dom';
import { PhotoCamera } from '@mui/icons-material';
import {authFirebase} from '../features/firebase'
import { createUserWithEmailAndPassword } from 'firebase/auth';
const Register = ({ role }) => {
  const [email, setEmail] = useState('');
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [fullname, setFullname] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [file, setFile] = useState([]);
  const [errors, setErrors] = useState({});
  const navigate = useNavigate();
  const [avatarPreview, setAvatarPreview] = useState(null);

  const handleFileInputChange = (event) => {
    const file = event.target.files[0];
    if (file) {
      const reader = new FileReader();
      reader.onloadend = () => {
        setAvatarPreview(reader.result); // Cập nhật hình ảnh xem trước
      };
      reader.readAsDataURL(file); // Đọc file để hiển thị preview
    }
    handleFileChange(event); // Gọi hàm xử lý file truyền từ props
  };
  const validate = () => {
    let templateErrors = {};
    if (!email) {
      templateErrors.email = "Vui lòng nhập Email";
    } else if (!/\S+@\S+\.\S+/.test(email)) {
      templateErrors.email = "Email không hợp lệ";
    }
    if (!username) {
      templateErrors.username = "Vui lòng nhập Tên người dùng";
    }

    // Validate password
    if (!password) {
      templateErrors.password = "Vui lòng nhập Mật khẩu";
    } else if (password.length < 6) {
      templateErrors.password = "Mật khẩu phải có ít nhất 6 ký tự";
    }

    // Validate confirmPassword
    if (!confirmPassword) {
      templateErrors.confirmPassword = "Vui lòng xác nhận mật khẩu";
    } else if (password !== confirmPassword) {
      templateErrors.confirmPassword = "Mật khẩu xác nhận không khớp";
    }

    // Validate fullname
    if (!fullname) {
      templateErrors.fullname = "Vui lòng nhập Họ Tên";
    }

    // Validate file (if it's required)
    if (file.length === 0) {
      templateErrors.file = "Vui lòng tải lên một tệp";
    }

    // Update errors state
    setErrors(templateErrors);

    // Return a boolean indicating whether the form is valid or not
    return Object.keys(templateErrors).length === 0;
  }

  const handleFileChange = (e) => {
    setFile(e.target.files[0]); 
    console.log(e.target.files[0])
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!validate()) return;
    try {
      const formData = new FormData();

      formData.append('email', email);
      formData.append('username', username);
      formData.append('password', password);
      formData.append('fullname', fullname);
      formData.append('userRole', role);
      formData.append('avatar', file); 

      const response = await APIs.post(endpoints['register'], formData, {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      });
      console.log('User added successfully:', response.data);
    navigate("/login")
    } catch (error) {
      console.error('Error adding rental:', error);
    };

    try {
      const userCredential = await createUserWithEmailAndPassword(authFirebase, email, password);
      console.log('User created:', userCredential.user);
    } catch (err) {
      console.log(err.message);
    }
  }

  return (
    <Container maxWidth="sm">
      <Paper elevation={3} sx={{ padding: 3, marginTop: 3 }}>
        {role === 'HOST' ?
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
            ĐĂNG KÝ CHỦ TRỌ
          </Typography>
          :
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
            ĐĂNG KÝ NGƯỜI THUÊ
          </Typography>}

        <Box component="form" onSubmit={handleSubmit} noValidate sx={{ mt: 1 }}>

          {/* Username Field */}
          <TextField
            required
            fullWidth
            id="username"
            label="Tên đăng nhập"
            name="username"
            autoComplete="username"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
            sx={{ mb: 1 }}
            error={Boolean(errors.username)}
            helperText={errors.username && errors.username}
          />

          {/* Email Field */}
          <TextField
            required
            fullWidth
            id="email"
            label="Email"
            name="email"
            autoComplete="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            sx={{ mb: 1 }}
            error={Boolean(errors.email)}
            helperText={errors.email && errors.email}
          />

          {/* Fullname Field */}
          <TextField
            required
            fullWidth
            id="fullname"
            label="Họ tên"
            name="fullname"
            autoComplete="fullname"
            value={fullname}
            onChange={(e) => setFullname(e.target.value)}
            sx={{ mb: 1 }}
            error={Boolean(errors.fullname)}
            helperText={errors.fullname && errors.fullname}
          />

          {/* Password Field */}
          <TextField
            required
            fullWidth
            id="password"
            label="Mật khẩu"
            name="password"
            type="password"
            autoComplete="new-password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            sx={{ mb: 1 }}
            error={Boolean(errors.password)}
            helperText={errors.password && errors.password}
          />

          {/* Confirm Password Field */}
          <TextField
            required
            fullWidth
            id="confirm-password"
            label="Xác nhận mật khẩu"
            name="confirm-password"
            type="password"
            autoComplete="new-password"
            value={confirmPassword}
            onChange={(e) => setConfirmPassword(e.target.value)}
            sx={{ mb: 1 }}
            error={Boolean(errors.confirmPassword)}
            helperText={errors.confirmPassword && errors.confirmPassword}
          />
          <Avatar
            alt="Avatar Preview"
            src={avatarPreview}
            sx={{ width: 100, height: 100, margin: '0 auto' }}
          />
          <label htmlFor="avatar">
            <input
              accept="image/*"
              id="avatar"
              type="file"
              style={{ display: 'none' }}
              onChange={handleFileInputChange}
            />
            <Button
              variant="contained"
              component="span"
              startIcon={<PhotoCamera />}
              sx={{ marginTop: 2 }}
            >
              Upload Avatar
            </Button>
          </label>

          {/* Hiển thị lỗi nếu có */}
          {errors.file && <Typography color="error">{errors.file}</Typography>}

          {/* Submit Button */}
          <Button
            type="submit"
            fullWidth
            variant="contained"
            sx={{ mt: 3, mb: 2 }}
          >
            Đăng ký
          </Button>
        </Box>
      </Paper>
    </Container>
  );
};

export default Register;
