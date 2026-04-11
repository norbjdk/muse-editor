import './styles/global.css'
import { Route, Routes } from 'react-router-dom'
import { AuthProvider } from './context/AuthContext';
import PrivateRoute from './components/PrivateRoute';
import MainLayout from './layouts/MainLayout'
import NotFound from './views/NotFound';
import Home from './views/Home';
import Scores from './views/Scores';
import Login from './views/Login';
import Register from './views/Register';
import Profile from './views/Profile';
import AdminPanel from './views/AdminPanel';
import Upload from './views/Upload';
import Download from './views/Download';
import Support from './views/Support';


function App() {
  return(
    <AuthProvider>
      <Routes>
        <Route element={<MainLayout />}>
          <Route path='/' element={<Home />} />
          <Route path="/scores" element={<Scores />} />
          <Route path='/login' element={<Login />} />
          <Route path='/register' element={<Register />} />
          <Route path="/profile" element={<PrivateRoute><Profile /></PrivateRoute>} />
          <Route path="/admin" element={<AdminPanel />} />
          <Route path="/upload" element={<Upload />} />
          <Route path="/download" element={<Download />} />
          <Route path="/support" element={<Support />} />
          <Route path="*" element={<NotFound />} />
        </Route>
      </Routes>
    </AuthProvider>
  );
}

export default App;