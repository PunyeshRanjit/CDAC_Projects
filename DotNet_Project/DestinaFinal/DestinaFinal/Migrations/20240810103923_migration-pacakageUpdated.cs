using Microsoft.EntityFrameworkCore.Migrations;

#nullable disable

namespace DestinaFinal.Migrations
{
    /// <inheritdoc />
    public partial class migrationpacakageUpdated : Migration
    {
        /// <inheritdoc />
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.AddColumn<string>(
                name: "location",
                table: "Packages",
                type: "nvarchar(max)",
                nullable: false,
                defaultValue: "");
        }

        /// <inheritdoc />
        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropColumn(
                name: "location",
                table: "Packages");
        }
    }
}
