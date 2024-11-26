// src/ChatRoom.js
import React, { useEffect, useRef, useState } from 'react';
import { firestore, authFirebase } from '../features/firebase';
import { TextField, Button, List, ListItem, ListItemText, Paper, Typography, Grid, ListItemButton, Avatar } from '@mui/material';
import { collection, query, where, orderBy, addDoc, serverTimestamp, limit } from 'firebase/firestore';
import { useCollectionData } from 'react-firebase-hooks/firestore';
import { useSelector } from 'react-redux';
import cookie from "react-cookies";


const Chat = () => {
  const dummy = useRef();
  const user = cookie.load("userFirebase");
  const auth = useSelector((state) => state.auth.auth);

  const [selectedRoomId, setSelectedRoomId] = useState(null);
  const [newRoomName, setNewRoomName] = useState('');
  const [formValue, setFormValue] = useState('');

  const roomsRef = collection(firestore, 'rooms');
  const messagesRef = collection(firestore, 'messages');


  const roomsQuery = user ? query(roomsRef, where('users', 'array-contains', user.email)) : null;
  const [rooms] = useCollectionData(roomsQuery, { idField: 'id' });

  const messagesQuery = selectedRoomId
    ? query(
      messagesRef,
      where('roomId', '==', selectedRoomId),
      // orderBy('createdAt','asc'),
      limit(25)
    )
    : null;

  const [messages] = useCollectionData(messagesQuery, { idField: 'id' })

  const getRoomFullName = (room) => {
    if (!room || !room.users || !room.fullNames) return '';
    const userIndex = room.users.indexOf(user.email);  // Tìm chỉ số của người dùng hiện tại
    const otherUserIndex = userIndex === 0 ? 1 : 0;  // Lấy tên người đối phương (người còn lại)
    return room.fullNames[otherUserIndex];  // Trả về tên đầy đủ của người đối phương
  };

  useEffect(() => {
    if (rooms) {
      setSelectedRoomId(rooms[0].id)
      console.log(rooms);
    }
  }, [rooms]);
  const createRoom = async (e) => {
    e.preventDefault();
    const room = {
      id: user.email > "2151053019hieu@ou.edu.vn" ? user.email + "2151053019hieu@ou.edu.vn" : "2151053019hieu@ou.edu.vn" + user.email,
      roomName: newRoomName,
      createdAt: serverTimestamp(),
      users: [user.email, '2151053019hieu@ou.edu.vn'],
    };
    await addDoc(roomsRef, room);
    setNewRoomName('');
  };

 


  const sendMessage = async (e) => {
    e.preventDefault();

    if (formValue.trim() === "") {
      // Kiểm tra nếu nội dung tin nhắn rỗng hoặc chỉ chứa khoảng trắng
      setFormValue('')
      return; // Không làm gì nếu tin nhắn rỗng
    }
    const { uid } = user;

    const photoURL = auth.user.avatar;
    await addDoc(messagesRef, {
      roomId: selectedRoomId,
      text: formValue,
      createdAt: serverTimestamp(),
      uid,
      photoURL,
    });

    setFormValue('');
    dummy.current.scrollIntoView({ behavior: 'smooth' });
  };

  return (
    <Grid container>
      {/* Phòng Chat */}
      <Grid item xs={3}>
        <Paper elevation={3} style={{ height: '80vh', padding: '16px' }}>
          {/* <form onSubmit={createRoom}>
            <TextField
              value={newRoomName}
              onChange={(e) => setNewRoomName(e.target.value)}
              placeholder="New room name"
              fullWidth
              margin="normal"
              variant="outlined"
            />
            <Button type="submit" variant="contained" color="primary" fullWidth>
              Create Room
            </Button>
          </form> */}
          <List overflowY="auto">
            {rooms && rooms.map((room) => (
              <ListItemButton key={room.id} onClick={() => setSelectedRoomId(room.id)}>
                <ListItemText primary={getRoomFullName(room)} />
              </ListItemButton>
            ))}
          </List>
        </Paper>
      </Grid>

      {/* Tin Nhắn */}
      <Grid item xs={9}>
        {selectedRoomId ? (
          <Paper elevation={3} style={{ height: '80vh', padding: '16px', display: 'flex', flexDirection: 'column' }}>
            <main style={{ flexGrow: 1, overflowY: 'auto', marginBottom: '16px' }}>
              {messages && messages
                .filter((msg) => msg.createdAt)
                .sort((a, b) => a.createdAt.seconds - b.createdAt.seconds).map((msg) => <ChatMessage key={msg.id} message={msg} />)}
              <span ref={dummy}></span>
            </main>

            <form onSubmit={sendMessage} style={{ display: 'flex' }}>
              <TextField
                value={formValue}
                onChange={(e) => setFormValue(e.target.value)}
                placeholder="Say something nice"
                fullWidth
                variant="outlined"
              />
              <Button type="submit" variant="contained" color="primary">
                Send
              </Button>
            </form>
          </Paper>
        ) : (
          <Paper elevation={3} style={{ height: '100vh', padding: '16px' }}>
            <Typography variant="h5" align="center" color="textSecondary">
              Chưa có phòng chat nào
            </Typography>
          </Paper>
        )}
      </Grid>
    </Grid>
  );
};

const ChatMessage = (props) => {
  const user = cookie.load("userFirebase");
  const { text, uid, photoURL, createdAt } = props.message;
  const isOwner = uid === user.uid ;
  return (
    <div style={{ display:'flex', padding: '15px', marginBottom: '8px',justifyContent:isOwner ?'flex-end':'flex-start' }}>
      {!isOwner && <Avatar src={photoURL} style={{marginRight:'10px'}}/>}
      <Typography style={{ background: !isOwner?'#1976d2':'#eceeea', borderRadius:'50px', padding:'10px'}} variant="body1">{text}</Typography>
    </div>
  );
};


export default Chat;
