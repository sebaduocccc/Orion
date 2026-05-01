import NavBar from "./Navbar";
const Home = () => {

    return (

        <div>
            <NavBar />
            
            <div className="container mt-5">
                <h1>Bienvenido a la página de inicio</h1>
                <p>Esta es la página principal de tu aplicación.</p>
            </div>
        </div>

    );
}

export default Home;