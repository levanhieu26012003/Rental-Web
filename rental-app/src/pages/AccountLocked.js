// AccountLocked.js
import React from 'react';
import { Container, Typography } from '@mui/material';

const AccountLocked = () => (
  <Container maxWidth="sm">
    <Typography variant="h4" color="error" align="center" sx={{ mt: 4, fontWeight: 'bold' }}>
      TÀI KHOẢN ĐÃ BỊ CẤM DO BỊ TỐ CÁO NHIỀU LẦN
    </Typography>
    <Typography variant="body1" align="center" sx={{ mt: 2 }}>
      Vui lòng liên hệ với chúng tôi qua email để biết thêm chi tiết.
    </Typography>
  </Container>
);

export default AccountLocked;
