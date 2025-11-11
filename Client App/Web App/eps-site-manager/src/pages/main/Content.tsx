
import { Outlet } from 'react-router-dom'; 

function Content() {
    return (
        <main className="content">
            <div className="container-fluid p-0">
                <Outlet />
            </div>
        </main>      
    );
}

export default Content;

