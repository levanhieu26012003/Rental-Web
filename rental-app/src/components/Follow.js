

import { Button, Card, Paper, Box } from "@mui/material";
import { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import APIs, { authApi,endpoints } from "../api/api";
import { fetchUserDetails } from "../features/auth/authSlice"
import { useNavigate } from "react-router-dom";


const Follower = ({ id }) => {
  const [isFollower, setIsFollower] = useState(false);
  const auth = useSelector((state) => state.auth.auth);
  const dispatch = useDispatch();
  const navigate = useNavigate()

  useEffect(() => {
    if (auth && auth.user && auth.user.following) {
      const userCollection = auth.user.following;
      const isFollowing = userCollection.some(user => user.id === id);
      setIsFollower(isFollowing);
    }
  }, [])

  const handleFollow = async () => {
    
    if (!auth.user){
      navigate("/login")
    }else{
      try {
        const respone = await authApi().post(endpoints['follow'], {
          followerId: auth.user.id.toString(),
          hostId: id
        })
        setIsFollower(true)
        await dispatch(fetchUserDetails())
      } catch (error) {
        console.log(error)
      }
    }
    
  }
  const handleUnFollow = async () => {
    try {
      const respone = await authApi().post(endpoints['unfollow'], {
        followerId: auth.user.id,
        hostId: id
      })
      setIsFollower(false)
      await dispatch(fetchUserDetails())
    } catch (error) {
      console.log(error)
    }
  }
  return (
    <Paper  elevation={3} sx={{ width:"150px",padding: 2 }}>
      <Box  display="flex" justifyContent="center" alignItems="center">
        {!isFollower ? (
          <Button variant="contained" color="primary" onClick={handleFollow}>
            Theo Dõi
          </Button>
        ) : (
          <Button variant="outlined" color="primary" onClick={handleUnFollow}>
            Hủy Theo Dõi
          </Button>
        )}
      </Box>
    </Paper>
  )
}

export default Follower;