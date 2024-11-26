import React from 'react';
import { AppBar, Toolbar, Typography, Container, Grid, Link, Box } from '@mui/material';
import { Facebook, Twitter, Instagram } from '@mui/icons-material';

const Footer = () => {
  return (
    <AppBar position="static" color="primary" sx={{mt:3, top: 'auto', bottom: 0, p: 2 }}>
      <Toolbar>
        <Container maxWidth="lg">
          <Grid container spacing={4} justifyContent="space-between">
            <Grid item xs={12} sm={4}>
              <Typography variant="h6" color="inherit" sx={{ mb: 1 }}>
                Rental App
              </Typography>
              <Typography variant="body2" color="inherit">
                © {new Date().getFullYear()} Rental App. All rights reserved.
              </Typography>
            </Grid>
            <Grid item xs={12} sm={4}>
              <Typography variant="h6" color="inherit" sx={{ mb: 1 }}>
                Quick Links
              </Typography>
              <Box>
                <Link href="/" color="inherit" variant="body2" sx={{ display: 'block', mb: 0.5 }}>
                  Home
                </Link>
                <Link href="/contact" color="inherit" variant="body2" sx={{ display: 'block' }}>
                  Contact
                </Link>
              </Box>
            </Grid>
            <Grid item xs={12} sm={4}>
              <Typography variant="h6" color="inherit" sx={{ mb: 1 }}>
                Follow Us
              </Typography>
              <Box sx={{ display: 'flex', gap: 1 }}>
                <Link href="https://facebook.com" color="inherit" aria-label="Facebook">
                  <Facebook />
                </Link>
                <Link href="https://twitter.com" color="inherit" aria-label="Twitter">
                  <Twitter />
                </Link>
                <Link href="https://instagram.com" color="inherit" aria-label="Instagram">
                  <Instagram />
                </Link>
              </Box>
            </Grid>
          </Grid>
        </Container>
      </Toolbar>
    </AppBar>
  );
};

export default Footer;
