import { AddPhotoAlternate, Close } from '@mui/icons-material'
import { Button, CircularProgress, Grid, IconButton, TextField } from '@mui/material'
import { useFormik } from 'formik'
import React, { useState } from 'react'
import { useDispatch } from 'react-redux'
import { createCategory } from '../../Redux/Category/action'
import { uploadToCloudinary } from "../../util/uploadToCloudinary";

const Categoryform = () => {
  const dispatch = useDispatch();
  const [uploadImage,setUploadImage] = useState(false);
  const formik =useFormik({
    initialValues:{
      name:"",
      image:""
    },
    onSubmit:(values)=>{
      dispatch(createCategory({category:values,jwt:localStorage.getItem("jwt")}))
      console.log("Submitting",formik.values);
    }
  })

  const handleImageChange=async(event)=>{
    const file = event.target.files[0]
    setUploadImage(true)
    const image = await uploadToCloudinary(file);
    formik.setFieldValue("image", image);
    setUploadImage(false)
  }
  return (
    <div className='flex justify-center items-center'>
      <form onSubmit={formik.handleSubmit} className='space-y-4 p-4 w-full lg:w-1/2'>
        <Grid container spacing={2}>
          <Grid className="w-24 h-24" size={12}>
            {formik.values.image ? <div className="relative border">
              <img className='w-24 h-24 object-cover' src={formik.values.image} alt="" srcset="" />

              <IconButton className='' color='error' size='small' sx={{position:"absolute",
                top:0,
                right:0
              }}>
                <Close sx={{fontSize:"1rem"}}/>
              </IconButton>


            </div> : <>
             <input type='file' accept='image/*' id='fileInput' style={{display:"none"}}
             onChange={handleImageChange}/>
             <label className='relative' htmlFor='fileInput'>
              <span className='w-24 h-24 cursor-pointer flex items-center justify-center p-3 border rounded-md border-gray-400'>
                <AddPhotoAlternate className='text-gray-700'/>
              </span>
              {uploadImage && <div className='absolute left-0 right-0 top-0 bottom-0 w-24 h-24 flex justify-center items-center'>
                <CircularProgress/>
              </div>}
            </label> 
            </> }
            

          </Grid>

          <Grid size={12}>
            <TextField fullWidth
            id="name"
            name="name"
            label=""
            value={formik.values.name}
            onChange={formik.handleChange} 
            required
            />
          </Grid>   
          <Grid size={12}>
                <Button type='submit'
                variant='outlined'
                fullWidth
                sx={{py: ".8rem"}} >
                  Create Category</Button>
          </Grid>     

        </Grid>

      </form>
    </div>
  )
}

export default Categoryform