
const Post = ({ post }) => {
    return(

        <div>
            <div className="card">
                <div className="card-header font-weight-bold">
                    {post.userId ? post.autorUsername : `Usuario ${post.autorID}`} {/* Muestra el nombre de usuario si está disponible, de lo contrario muestra el ID del autor */}
                    </div>
                <div className="card-body">
                    <p className="card-text">
                        {post.content}
                    </p>
                    </div>
                <div className="card-footer">
                    <button className="btn btn-primary">Like</button>
                </div>
            </div>
        </div>

    );
}

export default Post;