import React from 'react'
import { Box, Grid, LinearProgress, Rating } from "@mui/material";

const RatingRow = ({ label, value, count, color }) => {
    return (
        <Grid container alignItems="center" spacing={1} sx={{ mb: 1.5 }}>
            <Grid size={2.5}>
                <p className='m-0 leading-none' style={{ whiteSpace: 'nowrap' }}>{label}</p>
            </Grid>

            <Grid size={6.5} sx={{ display: 'flex', alignItems: 'center' }}>
                <LinearProgress
                    sx={{
                        bgcolor: "#d0d0d0",
                        height: 7,
                        borderRadius: 4,
                        width: '100%'
                    }}
                    variant='determinate'
                    value={value}
                    color={color}
                />
            </Grid>

            <Grid size={2}>
                <p className='m-0 opacity-50 pl-2 leading-none'>{count}</p>
            </Grid>
        </Grid>
    )
}

const RatingCard = () => {
    return (
        <div className='border p-5 rounded-md'>
            <div className='flex items-center space-x-3 pb-10'>
                <Rating
                    readOnly
                    value={4.5}
                    name="half-name"
                    defaultValue={4.5}
                    precision={0.5}
                />
                <p className='opacity-60 m-0'>45678</p>
            </div>

            <Box className="pl-5">
                <RatingRow label="Excellent" value={40} count={12993} color="success" />
                <RatingRow label="Very Good" value={50} count={12993} color="success" />
                <RatingRow label="Good" value={30} count={12993} color="warning" />
                <RatingRow label="Average" value={20} count={12993} color="success" />
                <RatingRow label="Poor" value={40} count={12993} color="error" />
            </Box>
        </div>
    )
}

export default RatingCard