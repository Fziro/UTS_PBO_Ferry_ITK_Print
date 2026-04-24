import java.util.Scanner

// Ferry Kurniawan - 04231033
// Tema 9: ITK-Print (Versi Final dengan Dashboard Status Lengkap)

class Pelanggan(private val _nama: String, private var _saldo: Double) {
    val nama: String get() = _nama
    val saldo: Double get() = _saldo

    fun tambahSaldo(jumlah: Double) {
        if (jumlah > 0) {
            _saldo += jumlah
            println("--- [QRIS SUCCESS] Saldo $nama bertambah Rp$jumlah. Total: Rp$_saldo ---")
        }
    }

    fun kurangiSaldo(jumlah: Double) {
        _saldo -= jumlah
    }
}

class MesinPrint(private var _tinta: Int, private var _kertas: Int) {
    private val hargaHitamPutih = 500
    private val hargaWarna = 1500

    // GETTER: Agar main() bisa membaca nilai stok tanpa bisa mengubahnya secara langsung
    val tinta: Int get() = _tinta
    val kertas: Int get() = _kertas

    fun cetak(pelanggan: Pelanggan, namaFile: String, hal: Int, isWarna: Boolean) {
        val tarif = if (isWarna) hargaWarna else hargaHitamPutih
        val totalBiaya = hal * tarif

        println("\n>> Memproses: $namaFile ($hal halaman | ${if(isWarna) "Warna" else "B&W"})")

        if (_tinta < hal || _kertas < hal) {
            println("!! Gagal: Stok mesin tidak cukup (Tinta: $_tinta, Kertas: $_kertas) !!")
        } else if (pelanggan.saldo < totalBiaya) {
            println("!! Gagal: Saldo kurang. Butuh Rp$totalBiaya, Saldo anda Rp${pelanggan.saldo} !!")
        } else {
            _tinta -= hal
            _kertas -= hal
            pelanggan.kurangiSaldo(totalBiaya.toDouble())

            println("SUCCESS: Dokumen berhasil dicetak!")
            println("INFO -> Sisa Saldo: Rp${pelanggan.saldo} | Sisa Tinta: $_tinta | Sisa Kertas: $_kertas")
        }
    }
}

fun main() {
    val sc = Scanner(System.`in`)
    val mesin = MesinPrint(100, 100)

    println("=== SELAMAT DATANG DI ITK-PRINT ===")
    print("Masukkan Nama Anda: ")
    val namaInput = sc.nextLine()
    val user = Pelanggan(namaInput, 0.0)

    var isRunning = true
    while (isRunning) {
        println("\nMenu Utama Pelanggan:")
        println("1. Top Up Saldo (QRIS)")
        println("2. Cetak Dokumen")
        println("3. Cek Status Akun & Mesin")
        println("4. Keluar")
        print("Pilih menu: ")

        if (!sc.hasNextInt()) {
            println("Input harus berupa angka!")
            sc.next()
            continue
        }

        when (sc.nextInt()) {
            1 -> {
                print("Masukkan nominal Top Up: Rp")
                user.tambahSaldo(sc.nextDouble())
            }
            2 -> {
                sc.nextLine() // clear buffer
                print("Nama File: ")
                val file = sc.nextLine()
                print("Jumlah Halaman: ")
                val hal = sc.nextInt()
                println("Jenis Cetak: 1. Hitam Putih (Rp500) | 2. Warna (Rp1500)")
                val jenis = sc.nextInt()

                mesin.cetak(user, file, hal, jenis == 2)
            }
            3 -> {
                // TAMPILAN BARU: Status Pelanggan + Status Mesin
                println("\n=== DASHBOARD STATUS ITK-PRINT ===")
                println("Nama Pelanggan : ${user.nama}")
                println("Saldo Akun     : Rp${user.saldo}")
                println("-----------------------------------")
                println("Kondisi Mesin Saat Ini:")
                println("- Stok Tinta    : ${mesin.tinta} ml")
                println("- Stok Kertas   : ${mesin.kertas} lembar")
                println("===================================")
            }
            4 -> isRunning = false
            else -> println("Pilihan tidak valid.")
        }
    }
    println("Terima kasih telah menggunakan ITK-Print, ${user.nama}!")
}