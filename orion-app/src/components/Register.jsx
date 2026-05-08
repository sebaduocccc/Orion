import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";

const Register = () => {


    const [username, setUsername] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [biografia, setBiografia] = useState('');
    const [avatarUrl, setAvatarUrl] = useState('');
    const [ubicacion, setUbicacion] = useState('');

    const [error, setError] = useState(null);

    const navigate = useNavigate();



    const handleSubmit = async (e) => {
        e.preventDefault(); // no recargue pagina al mandar el formulario
        setError(null); // resetea el error antes de intentar registrar

        try{

            const response = await fetch(`${import.meta.env.VITE_API_BASE_URL}/api/usuarios/registro`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ username, email, password, biografia, ubicacion, avatarUrl })
            });

            if (!response.ok) {
                throw new Error('Registro fallido'); // manda mensaje de error en vivo.
            }

            const data = await response.json(); // Se obtiene la respuesta de la API en formato JSON
            navigate('/login'); // Redirige a la página de login después del registro exitoso


        } catch (err) {
            setError(err.message); // Si hay un error, se muestra el mensaje de error en la interfaz
        }


    };




    return (
        <div className="containter mt-5">
            <div className="row justify-content-center">
                <div className="col-md-6">
                    <div className="card">

                        <div className="card-header">
                            <h1>Orion</h1>
                        </div>

                        <div className="card-body">
                            <h2>Register</h2>
                            {error && <div className="alert alert-danger">{error}</div>} {/* Muestra el mensaje de error si existe */}
                            <form onSubmit={handleSubmit}>
                                <div className="mb-3">
                                <label className="form-label">Username</label>
                                <input type="text" 
                                       className="form-control"
                                       value={username}
                                       onChange={(e) => setUsername(e.target.value)}
                                       required
                                />
                            </div>

                            <div className="mb-3">
                                <label className="form-label">Email</label>
                                <input type="text" 
                                       className="form-control"
                                        value={email}
                                        onChange={(e) => setEmail(e.target.value)}
                                       required
                                />
                            </div>

                            <div className="mb-3">
                                <label className="form-label">Contraseña</label>
                                <input type="text" 
                                       className="form-control"
                                       value={password}
                                       onChange={(e) => setPassword(e.target.value)}
                                       required
                                />
                            </div>

                            <div className="mb-3">
                                <label className="form-label">Confirmar Contraseña</label>
                                <input type="text" 
                                       className="form-control"
                                       value={confirmPassword}
                                       onChange={(e) => setConfirmPassword(e.target.value)}
                                       required
                                />
                            </div>

                            <div className="mb-3">
                                <label className="form-label">Biografia</label>
                                <textarea className="form-control"
                                          value={biografia}
                                          onChange={(e) => setBiografia(e.target.value)}
                                />
                            </div>

                            <div className="mb-3">
                                <label className="form-label">Ubicacion</label>
                                <input type="text" 
                                       className="form-control"
                                       value={ubicacion}
                                       onChange={(e) => setUbicacion(e.target.value)}
                                       required
                                />
                            </div>
                            <Link to="/login" className="d-block mb-3">¿Ya tienes una cuenta? Inicia sesión aquí</Link>

                            <button type="submit" className="btn btn-primary w-100">Register</button>
                            </form>

                            
                        </div>

                        

                    </div>
                </div>
            </div>
        </div>
    );
};

export default Register;