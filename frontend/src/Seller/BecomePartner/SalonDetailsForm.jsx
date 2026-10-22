
import { AddPhotoAlternate, Close } from '@mui/icons-material'
import {
    CircularProgress,
    Container,
    IconButton,
    TextField,
    Typography
} from '@mui/material'

import React, { useState } from 'react'
import { uploadToCloudinary } from '../../util/uploadToCloudinary'
import { LocalizationProvider } from '@mui/x-date-pickers/LocalizationProvider'
import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs'
import { DateTimePicker } from '@mui/x-date-pickers/DateTimePicker'
import { TimePicker } from '@mui/x-date-pickers/TimePicker'
import dayjs from 'dayjs'


// const getLocalTime = (time) => {
//     let hour = time?.$H;
//     let minute = time?.$m;
//     let second = time?.$s;

//     const localTime = `${String(hour).padStart(2,'0')}:${String(
//         minute
//     ).padStart(2,"0")}:${String(second).padStart(2,'0')}`


//     return localTime;
// }

const SalonDetailsForm = ({ formik }) => {
    const [uplaodImage, setUploadImage] = useState(false)

    const handleImageChange = async (event) => {
        const file = event.target.files[0]
        setUploadImage(true)

        const image = await uploadToCloudinary(file)

        formik.setFieldValue(
            "salonDetails.images",
            [...formik.values.salonDetails.images, image]
        )

        setUploadImage(false)
    }

    const handeleRemoveImage = (index) => () => {
        const updatedImages = [...formik.values.salonDetails.images]
        updatedImages.splice(index, 1)

        formik.setFieldValue(
            "salonDetails.images",
            updatedImages
        )
    }

    return (
        <Container component={"main"}>
            <div className="space-y-5">
                <Typography className="text-center" variant="h5">
                    Salon Details
                </Typography>

                <form
                    className="space-y-5"
                    onSubmit={formik.handleSubmit}
                >
                    <div className="flex gap-5 flex-wrap">
                        {
                            formik.values.salonDetails.images.map(
                                (image, index) => (
                                    <div className="relative border">
                                        <img
                                            className="w-24 h-24 object-cover"
                                            src={image}
                                            alt=""
                                            srcSet=""
                                        />

                                        <IconButton
                                            onClick={handeleRemoveImage(index)}
                                            className=""
                                            color="error"
                                            size="small"
                                            sx={{
                                                position: "absolute",
                                                top: 0,
                                                right: 0
                                            }}
                                        >
                                            <Close sx={{ fontSize: "1rem" }} />
                                        </IconButton>
                                    </div>
                                )
                            )
                        }

                        <>
                            <input
                                type="file"
                                accept="image/*"
                                id="fileInput"
                                style={{ display: "none" }}
                                onChange={handleImageChange}
                            />

                            <label
                                className="relative"
                                htmlFor="fileInput"
                            >
                                <span className="w-24 h-24 cursor-pointer flex items-center justify-center p-3 border rounded-md border-gray-400">
                                    <AddPhotoAlternate className="text-gray-700" />
                                </span>

                                {
                                    uplaodImage && (
                                        <div className="absolute left-0 right-0 top-0 bottom-0 w-24 h-24 flex justify-center items-center">
                                            <CircularProgress />
                                        </div>
                                    )
                                }
                            </label>
                        </>
                    </div>

                    <TextField
                        variant="outlined"
                        fullWidth
                        name="salonDetails.name"
                        id="Salon Name"
                        label="Full Name"
                        onChange={formik.handleChange}
                        value={formik.values.salonDetails.name}
                        required
                    />

                    <LocalizationProvider
                        fullWidth
                        dateAdapter={AdapterDayjs}
                    >
                        <TimePicker
                            sx={{ width: "100%" }}
                            fullWidth
                            onChange={(value) => {
                                if (value) {
                                    formik.setFieldValue(
                                        "salonDetails.openTime",
                                        dayjs(value)
                                    )
                                }
                            }}
                            label="Select Open Time"
                        />
                    </LocalizationProvider>

                    <LocalizationProvider
                        fullWidth
                        dateAdapter={AdapterDayjs}
                    >
                        <TimePicker
                            sx={{ width: "100%" }}
                            fullWidth
                            onChange={(value) => {
                                if (value) {
                                    formik.setFieldValue(
                                        "salonDetails.closeTime",
                                        dayjs(value)
                                    )
                                }
                            }}
                            label="Select Close Time"
                        />
                    </LocalizationProvider>
                </form>
            </div>
        </Container>
    )
}

export default SalonDetailsForm
