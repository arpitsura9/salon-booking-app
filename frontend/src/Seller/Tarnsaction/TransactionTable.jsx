import { styled } from '@mui/material/styles';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell, { tableCellClasses } from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Paper from '@mui/material/Paper';
import { useDispatch, useSelector } from 'react-redux';
import { useEffect } from 'react';
import { fetchSalonBookings } from '../../Redux/Booking/action';

const StyledTableCell = styled(TableCell)(({ theme }) => ({
  [`&.${tableCellClasses.head}`]: {
    backgroundColor: theme.palette.common.black,
    color: theme.palette.common.white,
  },
  [`&.${tableCellClasses.body}`]: {
    fontSize: 14,
  },
}));

const StyledTableRow = styled(TableRow)(({ theme }) => ({
  '&:nth-of-type(odd)': {
    backgroundColor: theme.palette.action.hover,
  },
  // hide last border
  '&:last-child td, &:last-child th': {
    border: 0,
  },
}));

function createData(
  name: string,
  calories: number,
  fat: number,
  carbs: number,
  protein: number,
) {
  return { name, calories, fat, carbs, protein };
}

const rows = [
  createData('Frozen yoghurt', 159, 6.0, 24, 4.0),
  createData('Ice cream sandwich', 237, 9.0, 37, 4.3),
  createData('Eclair', 262, 16.0, 24, 6.0),
  createData('Cupcake', 305, 3.7, 67, 4.3),
  createData('Gingerbread', 356, 16.0, 49, 3.9),
];

export default function TransactionTables() {
  const {booking,salon} = useSelector((store)=>store)
  const dispatch = useDispatch();

  useEffect(()=> {
    if(salon.salon){
        dispatch(fetchSalonBookings({jwt:localStorage.getItem("jwt")}))
  
    }
  },[salon.salon])
  return (
    <>
    <h1 className='pb-5 font-bold text-xl'> Transactions</h1>
    <TableContainer component={Paper}>
      <Table sx={{ minWidth: 700 }} aria-label="customized table">
        <TableHead>
          <TableRow>
            <StyledTableCell>Date</StyledTableCell>
            <StyledTableCell align="right">Customer Details</StyledTableCell>
            <StyledTableCell align="right">Booking</StyledTableCell>
            <StyledTableCell align="right">Amount</StyledTableCell>
            
          </TableRow>
        </TableHead>
        <TableBody>
          {booking.bookings.map((item) => (
            <TableRow
            key={item.name}
            sx={{ '&:last-child td, &:last-child th' :{ border: 0}}}
            >
              <TableCell component="th" scope="row">
                {item.startTime}
              </TableCell>
              <TableCell align='right'>
                {item.user.email}
              </TableCell>
              <TableCell align='right'>
                {item.id}
              </TableCell>
              <TableCell align='right'>
                {item.totalPrice}
              </TableCell>

            </TableRow>
          ))}
        </TableBody>
      </Table>
    </TableContainer>
    </>
  );
}