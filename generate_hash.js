// Generar hash BCrypt para "fitness123"
const bcrypt = require('bcryptjs');

const password = 'fitness123';
const salt = bcrypt.genSaltSync(10);
const hash = bcrypt.hashSync(password, salt);

console.log('Password:', password);
console.log('Hash BCrypt:', hash);
console.log('');
console.log('Verificación:', bcrypt.compareSync(password, hash) ? '✓ Correcto' : '✗ Error');
