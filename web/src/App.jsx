import './styles/global.css'
import { Route, Routes } from 'react-router-dom'
import { AuthProvider } from './context/AuthContext';
import MainLayout from './layouts/MainLayout'
import Home from './views/Home';

function App() {
  return(
    <AuthProvider>
      <Routes>
        <Route element={<MainLayout />}>
          <Route path='/' element={<Home />} />
        </Route>
      </Routes>
    </AuthProvider>
  );
}

export default App;