import { BrowserRouter, Routes, Route } from 'react-router-dom';
import './App.css'

import Layout from './components/structural/Layout';
import Home from './components/content/Home';
import PageNotFound from './components/content/PageNotFound';
import Login from './components/auth/Login';
import Register from './components/auth/Register';

function App() {
    return (
        <BrowserRouter>
            <Routes>
                <Route path="/" element={<Layout />}>
                    <Route index element={<Home />} />
                    <Route path="login" element={<Login />} />
                    <Route path="register" element={<Register />} />
                    <Route path="*" element={<PageNotFound />} />
                </Route>
            </Routes>
        </BrowserRouter>
    );
}

export default App
