
const Post = ({ autorID, contenido}) => {
    return(

        <div>
            <div className="card">
                <div className="card-header font-weight-bold">
                    @{autorID || 'usuario'}
                </div>
                <div className="card-body">
                    <p className="card-text">
                        {contenido}
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