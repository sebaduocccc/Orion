import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import Login from "./components/Login";
import Home from "./components/Home";

function App() {
  return(

    <BrowserRouter>
      <div className="bg-light min-vh-100">
        <Routes>

          {/* redirigir raiz al /login que es declarado mas abajo */}
          <Route path="/" element={<Navigate to="/login" />} />

          {/* declarado el endpoint de /login */}
          <Route path="/login" element={<Login />} />

          {/* declarado el endpoint de /home */}
          <Route path="/home" element={<Home />} />

        </Routes>
      </div>
    </BrowserRouter>

  );

}

export default App;