import { useState, useEffect, use} from "react";

const Test = () => {

    const [likesCount, setLikesCount] = useState(0); // Estado para almacenar el número de likes

    useEffect(() => {
        const fetchLikesCount = async () => {

            const token = localStorage.getItem('token');
            if (!token) return;

            try {
                const response = await fetch(`http://localhost:8000/api/interacciones/post/1/like/count`, { // Reemplaza '1' con el ID del post que deseas consultar
                    method: 'GET',
                    headers: {
                        'Authorization': `Bearer ${token}`
                    }
                });

                if (response.ok){
                    const data = await response.json();
                    console.log('Likes count: ', data);
                    setLikesCount(data);
                } else {
                    console.error('Error fetching likes count: ', response.status);
                }
            } catch (error) {
                console.error('Error fetching likes count:', error);
            }

        };
        fetchLikesCount();

    },[]);

    return(
        <div>
            <h1>ola: {likesCount}</h1>
        </div>
    );
};


export default Test;
