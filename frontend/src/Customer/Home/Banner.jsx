import React from "react";

const Banner = () => {
  return (
    <div className="relative w-full h-[80vh]">
      <video
        className="w-full h-full object-cover"
        muted
        autoPlay
        loop
        playsInline
        src="https://booksy-public.s3.amazonaws.com/horizontal_.webm"
      />

      <div className="absolute inset-0 z-20 flex flex-col items-center justify-center space-y-3 px-5 text-white">
        <h1 className="text-5xl font-bold">
          Be yourself
        </h1>

        <p className="text-2xl font-semibold text-center text-slate-400">
          Discover and book beauty & wellness near you
        </p>

        <input
          className="border-none bg-white w-[15rem] md:w-[33rem] rounded-md bg-white px-5 py-4 text-black outline-none "
          type="text"
          placeholder="Search salon services..."
        />
      </div>
    </div>
  );
};

export default Banner;