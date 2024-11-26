
import React, { useEffect } from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import Navbar from './components/Navbar';
import Footer from './components/Footer';
import Rentals from './pages/Rentals';
import AddRental from './pages/AddRental';
import Login from './pages/Login';
import Chat from './pages/Chat';
import Register from './pages/Register';
import MotelDetai from './components/RentailDetail';
import UserDetail from './components/UserDetail';
import { fetchUserDetails, loginUser } from './features/auth/authSlice';
import { useDispatch, useSelector } from 'react-redux';
import Followers from './pages/Followers';
import UpgradeAccount from './pages/UpgradeAccount';
import AccountLocked from './pages/AccountLocked';
import ProtectedRoute from './features/ProtectedRoute'
import CustomAutocomplete from './pages/test';


const App = () => {
  const auth = useSelector((state) => state.auth.auth)
  const dispatch = useDispatch();
  const isAuthenticated = useSelector((state) => state.auth.auth.isAuthenticated);

  useEffect(() => {
    if (isAuthenticated) {
      dispatch(fetchUserDetails());
    }
  }, [isAuthenticated, dispatch]);
  return (<Router>

    <div>
      <Navbar />
      <Routes>
        <Route exact path="/" element={<Rentals />} />
        <Route path="/add-rental"  element={<ProtectedRoute><AddRental /></ProtectedRoute>}/>
        <Route path="/login" element={<Login />} />
        <Route path="/registerHost" element={<Register role={"HOST"} />} />
        <Route path="/registerTenant" element={<Register role={"TENANT"} />} />
        <Route path="/motel/:id" element={<MotelDetai />} />
        <Route path="/user/:id" element={<UserDetail />} />
        <Route path="/chat"  element={<ProtectedRoute><Chat /></ProtectedRoute>}/>
        <Route path="/upgradeAccount" element={<UpgradeAccount />} />
        <Route path='/follower' element={<Followers users={auth && auth.user && auth.user.follower ? auth.user.follower : []} />} />
        <Route path='/following' element={<Followers users={auth && auth.user && auth.user.following ? auth.user.following : []} />} />
        <Route path="/account-locked" element={<AccountLocked />} />

      </Routes>
      <Footer />
    </div>
  </Router>
  )
};

export default App;

