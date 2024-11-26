
import React, { useEffect, useState } from 'react';
import { useQuery, useMutation, gql } from '@apollo/client';
import { Button, RadioGroup, FormControlLabel, Radio, Typography, Container } from '@mui/material';
import { authApi, endpoints } from '../api/api'
import { useSelector } from 'react-redux';
import CircularIndeterminate from './Spiner';
import { toast, ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
// GraphQL query để lấy tất cả các gói VIP
const GET_ALL_VIP_PACKAGES = gql`
  query GetAllVipPackages {
    getAllVipPackages {
      id
      type
      price
      limitTime
      upgradeTime
    }
  }
`;

const UpgradeAccount = () => {
  const { loading, error, data } = useQuery(GET_ALL_VIP_PACKAGES);
  const [selectedPackage, setSelectedPackage] = useState(null);
  const auth = useSelector((state) => state.auth.auth)
  const notify = () => {
    toast.warn("This is a warning message!", {
      position: "top-right",
      autoClose: 5000,
      hideProgressBar: false,
      closeOnClick: true,
      pauseOnHover: true,
      draggable: true,
      progress: undefined,
    });
  };
  if (loading) return <CircularIndeterminate>Error loading packages!</CircularIndeterminate>;
  if (error) return <p>Error loading packages!</p>;

  const handleSelectPackage = (event) => {
    setSelectedPackage(event.target.value);
  };



  const handleSubmit = async () => {
    if (!selectedPackage) {
      toast.warning("Vui lòng chọn gói");
      return;
    }

    try {
      const r = await authApi().get(endpoints['package'], {
        params: {
          id: auth.user.id,
          vipPackageId: Number(selectedPackage)
        }
      });
      if (r.data !== "Có thể đăng ký")
        toast.warning(r.data)
      else {

        try {
          const r = await authApi().post(endpoints['bill'], {
            userId: auth.user.id,
            vipPackageId: Number(selectedPackage)
          });
          const redirectUrl = r.data.order_url
          console.log(redirectUrl)
          window.location.href = redirectUrl; // Điều hướng đến URL của ZaloPay

        } catch (error) {
          console.error("Error upgrading to VIP:", error);
        }
      }

    } catch (error) {
      console.error("Error upgrading to VIP:", error);

    }



  };


  return (
    <Container>
      <Typography marginTop={"10px"} variant="h4">Chọn gói nâng cấp</Typography>
      <RadioGroup value={selectedPackage} onChange={handleSelectPackage}>
        {data.getAllVipPackages.map((vipPackage) => (
          <FormControlLabel
            key={vipPackage.id}
            value={vipPackage.id}
            control={<Radio />}
            label={`${vipPackage.type} - ${vipPackage.price} VND - ${vipPackage.upgradeTime} ngày`}
          />
        ))}
      </RadioGroup>
      <Button variant="contained" color="primary" onClick={handleSubmit}>
        Thanh toán
      </Button>
      <ToastContainer />

    </Container>
  );
}

export default UpgradeAccount;