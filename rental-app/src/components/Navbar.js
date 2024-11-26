
import React, { useEffect, useState } from 'react';
import { AppBar, Toolbar, Typography, Button, IconButton, Box, useMediaQuery, useTheme, Avatar, Menu, MenuItem } from '@mui/material';
import { NavLink } from 'react-router-dom';
import MenuIcon from '@mui/icons-material/Menu';
import Drawer from '@mui/material/Drawer';
import List from '@mui/material/List';
import ListItem from '@mui/material/ListItem';
import { useDispatch, useSelector } from 'react-redux';
import { logout } from '../features/auth/authSlice';
import cookie from "react-cookies";
import { useNavigate } from 'react-router-dom';

const Navbar = () => {
  const theme = useTheme();
  const isMobile = useMediaQuery(theme.breakpoints.down('md'));
  const [open, setOpen] = React.useState(false);
  const auth = useSelector((state) => state.auth.auth);
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const [anchorEl, setAnchorEl] = useState(null);
  const open1 = Boolean(anchorEl);

  // useEffect(() => {
  //   // if (auth.user != null) {
  //   console.log('hi')
  //     console.log("auth nav", auth.role)
  //   // }
  // }, [auth])
  // const handleCheck = (event) => {
  //   console.log(auth)
  // };
  const handleClick = (event) => {
    setAnchorEl(event.currentTarget);
  };
  const handleClose = () => {
    setAnchorEl(null);
  };

  const handleLogout = () => {
    dispatch(logout())
    setAnchorEl(null);
    navigate('/login');
  }

  const toggleDrawer = (open) => (event) => {
    if (event.type === 'keydown' && (event.key === 'Tab' || event.key === 'Shift')) {
      return;
    }
    setOpen(open);
  };
  const handleFollower = () => {
    navigate("/follower")
  }
  const handleFollowing = () => {
    navigate("/following")
  }

  const handleUpgradeAccount = () => {
    navigate("/upgradeAccount")
  }

  const handleRentalDetail = () => {
    navigate(`/user/${auth.user.id}`);  }

  const menuItems = (
    <List>
      <ListItem button component={NavLink} to="/" onClick={() => setOpen(false)}>
        Nhà Trọ
      </ListItem>
      <ListItem button component={NavLink} to="/login" onClick={() => setOpen(false)}>
        Đăng nhập
      </ListItem>
      <ListItem button component={NavLink} to="/registerHost" onClick={() => setOpen(false)}>
        Trở thành chủ trọ
      </ListItem>
      <ListItem button component={NavLink} to="/registerTenant" onClick={() => setOpen(false)}>
        Trở thành người thuê
      </ListItem>
    </List>
  );
  return (
    <AppBar position="static">
      <Toolbar>
        <Typography variant="h6" sx={{ flexGrow: 1 }}>
          Rental Web
        </Typography>
        {isMobile ? (
          <>
            <IconButton edge="start" color="inherit" onClick={toggleDrawer(true)} sx={{ mr: 2 }}>
              <MenuIcon />
            </IconButton>
            <Drawer anchor="right" open={open} onClose={toggleDrawer(false)}>
              {menuItems}
            </Drawer>
          </>
        ) : (
          <Box sx={{ display: 'flex', gap: 2 }}>
            <Button color="inherit" component={NavLink} to="/">
              Nhà trọ
            </Button>
            {auth.user && auth.user.userrole === "HOST" && (
              <Button color="inherit" component={NavLink} to="/add-rental">
                Đăng Bài
              </Button>
            )}
            {auth.user != null ?
              <Box>
                {/* Avatar và Tên người dùng */}
                <Button color="inherit" component={NavLink} to="/chat">
                  Chat
                </Button>
                {/* <Button onClick={handleCheck}>CLICK</Button> */}

                <Button
                  startIcon={
                    <Avatar title={auth.user.fullname} alt={auth.user.username} src={auth.user.avatar} />
                  }
                  onClick={handleClick}
                >
                </Button>
                <Menu
                  anchorEl={anchorEl}
                  open={open1}
                  onClose={handleClose}
                  anchorOrigin={{
                    vertical: 'bottom',
                    horizontal: 'right',
                  }}
                  transformOrigin={{
                    vertical: 'top',
                    horizontal: 'right',
                  }}
                >
                  {/* Các lựa chọn trong Menu */}
                  <MenuItem onClick={handleClose}>Chỉnh sửa hồ sơ</MenuItem>
                  <MenuItem onClick={handleRentalDetail}>Quản lý nhà thuê</MenuItem>
                  <MenuItem onClick={handleUpgradeAccount}>Nâng cấp tài khoản</MenuItem>
                  <MenuItem onClick={handleFollowing}>Xem người đang Follow</MenuItem>
                  <MenuItem onClick={handleFollower}>Xem Followers</MenuItem>
                  <MenuItem onClick={handleLogout}>Đăng xuất</MenuItem>
                </Menu>
              </Box>
              :
              <Box sx={{ ml: 'auto' }}>
                <Button color="inherit" component={NavLink} to="/login">
                  Đăng nhập
                </Button>

                <Button color="inherit" component={NavLink} to="/registerHost">
                  Trở thành chủ trọ
                </Button>
                <Button color="inherit" component={NavLink} to="/registerTenant">
                  Trở thành người thuê
                </Button>
              </Box>}
          </Box>
        )}
      </Toolbar>
    </AppBar>
  );
};

export default Navbar;

