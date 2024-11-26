import { Container } from "@mui/system";
import User from '../components/User'
import { Card, Box, Grid, Paper, Typography } from "@mui/material";
import Follower from "../components/Follow";
const Followers = ({ users }) => {
    console.log(users)
    return (
        <Container maxWidth="md">
            <Typography variant="h4" gutterBottom align="center" sx={{ mt: 4 }}>
                Danh sách người bạn đang theo dõi
            </Typography>
            {users.map((user, k) => (
                <Paper elevation={3} sx={{ mb: 3, padding: 2 }} key={k}>
                    <Box sx={{ flexGrow: 1 }}>
                        <Grid container alignItems="center" spacing={2}>
                            <Grid item xs={9}>
                                <User userId={user} />
                            </Grid>
                            <Grid item xs={3}>
                                <Follower id={user.id} />
                            </Grid>
                        </Grid>
                    </Box>
                </Paper>
            ))}

        </Container>
    )
}

export default Followers;