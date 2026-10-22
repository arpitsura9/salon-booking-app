import { Button } from '@mui/material'
import React, { useState } from 'react'
import CategoryTables from './CategoryTable';
import Categoryform from './Categoryform';


const Category = () => {
    const [activeTabs,setActiveTab] = useState(1);
    const handleTabClick= (tab) => () => setActiveTab(tab);
  return (
    <div>
        <div className="flex items-center gap-5">
            <Button 
            onClick={handleTabClick(1)}
            variant={activeTabs===1?"contained":"outlined"}>
                All Categories
            </Button>

            <Button 
            onClick={handleTabClick(2)}
            variant={activeTabs===2?"contained":"outlined"}>
                Category Categories
            </Button>
        </div>

        <div className="mt-10">
            {activeTabs === 1 ?<CategoryTables/>:<Categoryform/>}
        </div>
    </div>
  )
}

export default Category