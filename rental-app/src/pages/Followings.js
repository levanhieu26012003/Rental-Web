import { Container } from "@mui/system";
import User from '../components/User'
import { Box, Grid } from "@mui/material";
import Follower from "../components/Follow";
const Followings = ({ users }) => {
  return (
    <Container>
      <h1>Danh sách người đang theo dõi bạn</h1>
      {users.map((user, k) => (
        <Box sx={{ flexGrow: 1 }}>
          <Grid container spacing={2}>
            <Grid item xs={8}>
              <User userId={user} />
            </Grid>
            <Grid item xs={2}>
              <Follower id={user.id} />
            </Grid>
          </Grid>
        </Box>
      ))}
    </Container>
  )
}

export default Followings;