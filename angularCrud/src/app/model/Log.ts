export class Log {
  id_user?: number;
  email!: string;
  passwords!: string;
  role?: number;

  constructor(email: string, passwords: string, role?: number, id_user?: number) {
      this.id_user = id_user;
      this.email = email;
      this.passwords = passwords;
      this.role = role;
  }
}
