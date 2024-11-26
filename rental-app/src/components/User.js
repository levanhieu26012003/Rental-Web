import { Avatar, Box, Typography } from "@mui/material";
import { useNavigate } from "react-router-dom";


const User = ({userId}) => {
    const navigate = useNavigate(); // Khởi tạo useNavigate để điều hướng

    const handleCardClick = () => {
        // Điều hướng đến trang chi tiết của motel khi nhấp vào thẻ Card
        navigate(`/user/${userId.id}`);
      };
    return (
      <Box
      sx={{ cursor: 'pointer' }}
      display="flex"
      alignItems="center"
      mt={2}
      onClick={handleCardClick}
    >
      <Avatar alt={userId.fullname} src={userId.avatar} />
      <Box ml={2} color="text.secondary">
        <Typography variant="body1">{userId.fullname}</Typography>
      </Box>
    </Box>
    )
}

export default User;