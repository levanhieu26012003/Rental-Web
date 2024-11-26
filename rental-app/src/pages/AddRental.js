import React, { useCallback, useEffect, useRef, useState } from 'react';
import { authApi, endpoints } from "../api/api";
import { Container, TextField, Button, Typography, Box, Grid, Card, CardMedia, CardContent, Paper } from '@mui/material';
import { useDropzone } from 'react-dropzone';
import { useSelector } from 'react-redux';
import { GoogleMap, Marker, useLoadScript } from "@react-google-maps/api";
import { toast, ToastContainer } from "react-toastify";

const libraries = ["places"];
const mapContainerStyle = {
  width: "100%",
  height: "300px",
};

const initialCenter = {
  lat: 10.762622,
  lng: 106.660172, // Tọa độ của Thành phố Hồ Chí Minh
};

const options = {
  disableDefaultUI: true,
  zoomControl: true,
};

const AddRental = () => {
  const { isLoaded, loadError } = useLoadScript({
    googleMapsApiKey: process.env.REACT_APP_GOOGLE_MAPS_API_KEY, // Đặt API key của bạn ở đây
    libraries,
  });
  const [title, setTitle] = useState('');
  const [address, setAddress] = useState('');
  const [province, setProvince] = useState('');
  const [price, setPrice] = useState('');
  const [area, setArea] = useState('');
  const [district, setDistrict] = useState('');
  const [wards, setWards] = useState('');
  const [numberTenant, setNumberTenant] = useState('');
  const [files, setfiles] = useState([]);
  const [errors, setErrors] = useState({});
  const auth = useSelector((state) => state.auth.auth);
  const [selectedPosition, setSelectedPosition] = useState(initialCenter); // Vị trí marker
  const mapRef = useRef();
  const [currentIndex, setCurrentIndex] = useState(0);

  useEffect(() => {
    const interval = setInterval(() => {
      setCurrentIndex(1); // Vòng qua các index
    }, 1000);
    return () => clearInterval(interval);
  }, [])


  // Hàm xử lý sự kiện click trên bản đồ
  const onMapClick = useCallback((event) => {
    const lat = event.latLng.lat();
    const lng = event.latLng.lng();

    // Cập nhật vị trí marker
    setSelectedPosition({ lat, lng });

    // In tọa độ ra console
    console.log("Tọa độ điểm đã chọn:", { lat, lng });
  }, []);

  const onMapLoad = useCallback((map) => {
    mapRef.current = map;
  }, []);

  const handleDrop = (acceptedFiles) => {
    setfiles([...files, ...acceptedFiles.map(file => Object.assign(file, {
      preview: URL.createObjectURL(file)
    }))]);
  };

  const validate = () => {
    let tempErrors = {};
    if (!title) tempErrors.title = "Tiêu đề là bắt buộc";
    if (!area) tempErrors.title = "Diện tích là bắt buộc";
    if (!address) tempErrors.address = "Địa chỉ là bắt buộc";
    if (!province) tempErrors.province = "Tỉnh là bắt buộc";
    if (!price) tempErrors.price = "Giá là bắt buộc";
    if (!district) tempErrors.district = "Quận/Huyện là bắt buộc";
    if (!wards) tempErrors.wards = "Phường/Xã là bắt buộc";
    if (!numberTenant) tempErrors.numberTenant = "Số lượng người thuê là bắt buộc";
    if (files.length < 3) tempErrors.files = "Cần ít nhất 3 tệp";
    setErrors(tempErrors);
    return Object.keys(tempErrors).length === 0;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!validate()) return;
    try {
      const formData = new FormData();
      formData.append('title', title);
      formData.append('address', address);
      formData.append('province', province);
      formData.append('price', price);
      formData.append('area', area);
      formData.append('district', district);
      formData.append('wards', wards);
      formData.append('numberTenant', numberTenant);
      formData.append('lng', selectedPosition.lng);
      formData.append('lat', selectedPosition.lat);
      console.log(auth.user)
      formData.append('userId', auth.user.id);
      files.forEach(image => {
        formData.append('files', image);
      });
      for (let [key, value] of formData.entries()) {
        console.log(`${key}: ${value}`);
      }
      const response = await authApi().post(endpoints['createMotel'], formData, {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      });
      console.log('Rental added successfully:', response.data);
      toast.success(response.data)
    } catch (error) {
      console.error('Error adding rental:', error);
      toast.error(error)

    }
  };

  const { getRootProps, getInputProps } = useDropzone({
    accept: 'image/*',
    onDrop: handleDrop,
  });

  if (loadError) return "Lỗi khi tải bản đồ";
  if (!isLoaded) return "Đang tải bản đồ...";

  return (
    <Container maxWidth="md">
      <ToastContainer />
      <Paper elevation={3} sx={{ padding: 3, marginTop: 3 }}>
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
          Tin mới
        </Typography>
        <Box component="form" onSubmit={handleSubmit} noValidate sx={{ mt: 1 }}>
          <Grid container spacing={2}>
            <Grid item xs={12}>
              <TextField
                required
                fullWidth
                id="title"
                label="Tiêu đề"
                name="title"
                autoComplete="title"
                value={title}
                onChange={(e) => setTitle(e.target.value)}
                error={!!errors.title}
                helperText={errors.title}
              />
            </Grid>
            <Grid item xs={12}>
            <TextField
                required
                fullWidth
                id="price"
                label="Giá"
                name="price"
                autoComplete="price"
                value={price}
                onChange={(e) => setPrice(e.target.value)}
                error={!!errors.price}
                helperText={errors.price}
              />
          
            </Grid>
            <Grid item xs={12}>
              <TextField
                required
                fullWidth
                id="numberTenant"
                label="Số người tối đa"
                name="numberTenant"
                autoComplete="numberTenant"
                value={numberTenant}
                onChange={(e) => setNumberTenant(e.target.value)}
                error={!!errors.numberTenant}
                helperText={errors.numberTenant}
              />
            </Grid>
            <Grid item xs={12}>
              <TextField
                required
                fullWidth
                id="area"
                label="Diện tích"
                name="area"
                autoComplete="area"
                value={area}
                onChange={(e) => setArea(e.target.value)}
                error={!!errors.area}
                helperText={errors.area}
              />
            </Grid>
            <Grid item xs={12} sm={6}>
            <TextField
                required
                fullWidth
                id="address"
                label="Địa chỉ"
                name="address"
                autoComplete="address"
                value={address}
                onChange={(e) => setAddress(e.target.value)}
                error={!!errors.address}
                helperText={errors.address}
              />
            </Grid>
            <Grid item xs={12} sm={6}>
              <TextField
                required
                fullWidth
                id="wards"
                label="Phường/Xã/Thị Trấn"
                name="wards"
                autoComplete="wards"
                value={wards}
                onChange={(e) => setWards(e.target.value)}
                error={!!errors.wards}
                helperText={errors.wards}
              />
            </Grid>
            <Grid item xs={12} sm={6}>
              <TextField
                required
                fullWidth
                id="district"
                label="Quận/Huyện"
                name="district"
                autoComplete="district"
                value={district}
                onChange={(e) => setDistrict(e.target.value)}
                error={!!errors.district}
                helperText={errors.district}
              />
            </Grid>
            <Grid item xs={12} sm={6}>
              <TextField
                required
                fullWidth
                id="province"
                label="Tỉnh/TP"
                name="province"
                autoComplete="province"
                value={province}
                onChange={(e) => setProvince(e.target.value)}
                error={!!errors.province}
                helperText={errors.province}
              />
            </Grid>
           
            
            
            <Grid item xs={12}>
              <Box {...getRootProps()} sx={{ border: '2px dashed #ccc', padding: '20px', textAlign: 'center', cursor: 'pointer', marginTop: '20px' }}>
                <input {...getInputProps()} />
                <Typography variant="body1">Chọn tối thiểu 3 hình ảnh</Typography>
                {errors.files && <Typography variant="body2" color="error">{errors.files}</Typography>}
              </Box>
              <Grid container spacing={2} sx={{ marginTop: '20px' }}>
                {files.map((image, index) => (
                  <Grid item xs={12} sm={6} md={4} key={index}>
                    <Card>
                      <CardMedia
                        component="img"
                        alt={`Image ${index + 1}`}
                        height="140"
                        image={image.preview}
                      />
                      <CardContent>
                        <Typography variant="body2" color="text.secondary">{image.name}</Typography>
                      </CardContent>
                    </Card>
                  </Grid>
                ))}
              </Grid>
            </Grid>

            <Grid item xs={12}>
            <Typography variant="body1">Chọn địa điểm trên bản đồ</Typography>

              <GoogleMap
                mapContainerStyle={mapContainerStyle}
                zoom={15}
                center={selectedPosition} // Đặt center là vị trí hiện tại của marker
                options={options}
                onLoad={onMapLoad}
                onClick={onMapClick} // Xử lý sự kiện click
              >
                {/* Marker ở vị trí đã chọn */}
                <Marker key={`${currentIndex}` + 1}
                  position={selectedPosition} // Marker di chuyển theo vị trí đã chọn
                />
              </GoogleMap>
            </Grid>
            <Grid item xs={12}>
              <Button
                type="submit"
                fullWidth
                variant="contained"
                sx={{ mt: 3, mb: 2 }}
              >
                Add Rental
              </Button>
            </Grid>
          </Grid>
        </Box>
      </Paper>
    </Container>
  );
};

export default AddRental;
